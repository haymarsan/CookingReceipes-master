package com.hms.cookingreceipes.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hms.cookingreceipes.CookingApp.Companion.openMarket
import com.hms.cookingreceipes.R
import com.hms.cookingreceipes.adapter.BlogspotAdapter
import com.hms.cookingreceipes.data.model.Entry
import com.hms.cookingreceipes.databinding.ActivityHomeBinding
import com.hms.cookingreceipes.utils.AppUtils
import com.hms.cookingreceipes.utils.PreferenceUtils
import com.hms.cookingreceipes.viewmodel.CookingAppViewModel
import com.ironsource.adapters.supersonicads.SupersonicConfig
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSourceBannerLayout
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.BannerListener
import com.ironsource.mediationsdk.sdk.InterstitialListener

class HomeActivity : BaseActivity() {
    private val TAG = this.javaClass.simpleName
    private lateinit var mAppViewModel: CookingAppViewModel
    private val blogspotAdapter = BlogspotAdapter()
    private var isLoading: Boolean = false
    private lateinit var binding: ActivityHomeBinding

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAppViewModel = ViewModelProvider(this).get(CookingAppViewModel::class.java)

        binding.contentHome.recyclerMain.layoutManager = LinearLayoutManager(this)
        binding.contentHome.recyclerMain.recycledViewPool.setMaxRecycledViews(0, 0)
        binding.contentHome.recyclerMain.adapter = blogspotAdapter

        onLoadMoreData(1, 6)

        initIronSource()

        binding.contentHome.viewNestedScroll.setOnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (binding.contentHome.recyclerMain.visibility == View.VISIBLE) {
                if (v.getChildAt(v.childCount - 1) != null) {
                    if ((scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) &&
                        scrollY > oldScrollY
                    ) {
                        if (!isLoading) {
                            onLoadMoreData(
                                PreferenceUtils(this).loadEndPage() + 1,
                                PreferenceUtils(this).loadEndPage() + 6
                            )
                        }
                    }
                }
            }
        }

        blogspotAdapter.setOnItemClickListener(object : BlogspotAdapter.OnItemClickListener {
            override fun onItemClick(entry: Entry) {
                if (IronSource.isInterstitialReady()) {
                    IronSource.showInterstitial()
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.")
                }
                startActivity(WebPageActivity.newInstance(this@HomeActivity, entry))
            }

        })
    }

    private fun onLoadMoreData(start: Int, end: Int) {
        if (AppUtils().hasConnection(this)) {
            binding.mainProgress.visibility = View.VISIBLE
            val params = HashMap<String, String>()
            params["alt"] = "json"
            params["start-index"] = "$start"
            params["max-results"] = "$end"

            mAppViewModel.getBlogArticles(params).observe(this, Observer {
                if (it.feed.entry != null) {
                    blogspotAdapter.entryList += it.feed.entry
                    binding.mainProgress.visibility = View.GONE
                    PreferenceUtils(this).saveStartPage(start)
                    PreferenceUtils(this).saveEndPage(end)
                } else {
                    Toast.makeText(this, "No More contents", Toast.LENGTH_LONG).show()
                }
            })

        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_update -> openMarket(
                this,
                getString(R.string.app_url)
            )
        }
        return true
    }

    private fun initIronSource() {
        // Be sure to set a listener to each product that is being initiated

        //Network Connectivity Status
        IronSource.shouldTrackNetworkState(this, true)

        SupersonicConfig.getConfigObj().clientSideCallbacks = true

        // set the IronSource user id
        IronSource.setUserId(IronSource.getAdvertiserId(this))
        // init the IronSource SDK
        IronSource.init(
            this,
            getString(R.string.isAppKey),
            IronSource.AD_UNIT.BANNER,
            IronSource.AD_UNIT.INTERSTITIAL,
            IronSource.AD_UNIT.REWARDED_VIDEO
        )

        // In order to work with IronSourceBanners you need to add Providers who support banner ad unit and uncomment next line
        createAndloadBanner()
        createAndLoadInterstitial()
    }

    private lateinit var mIronSourceBannerLayout: IronSourceBannerLayout
    private fun createAndloadBanner() {

        // instantiate IronSourceBanner object, using the IronSource.createBanner API
        mIronSourceBannerLayout = IronSource.createBanner(this, ISBannerSize.BANNER)

        mIronSourceBannerLayout.let {
            // add IronSourceBanner to your container
            val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )

            binding.bannerContainer1.addView(it, 0, layoutParams)

            // set the banner listener
            it.bannerListener = object : BannerListener {
                override fun onBannerAdLoaded() {
                    Log.d(TAG, "onBannerAdLoaded")
                    // since banner container was "gone" by default, we need to make it visible as soon as the banner is ready
                    binding.bannerContainer1.visibility = View.VISIBLE
                }

                override fun onBannerAdLoadFailed(error: IronSourceError) {
                    Log.d(TAG, "onBannerAdLoadFailed $error")
                }

                override fun onBannerAdClicked() {
                    Log.d(TAG, "onBannerAdClicked")
                }

                override fun onBannerAdScreenPresented() {
                    Log.d(TAG, "onBannerAdScreenPresented")
                }

                override fun onBannerAdScreenDismissed() {
                    Log.d(TAG, "onBannerAdScreenDismissed")
                }

                override fun onBannerAdLeftApplication() {
                    Log.d(TAG, "onBannerAdLeftApplication")
                }
            }
            // load ad into the created banner
            IronSource.loadBanner(it)

        }
    }

    private fun createAndLoadInterstitial() {
        IronSource.loadInterstitial()
        IronSource.setInterstitialListener(object : InterstitialListener {
            override fun onInterstitialAdReady() {
                Log.d(TAG, "onInterstitialAdReady")
            }

            override fun onInterstitialAdLoadFailed(p0: IronSourceError?) {
                Log.d(TAG, "onInterstitialAdLoadFailed: ${p0!!.errorMessage}")
            }

            override fun onInterstitialAdOpened() {
                Log.d(TAG, "onInterstitialAdOpened")
            }

            override fun onInterstitialAdClosed() {
                Log.d(TAG, "onInterstitialAdClosed")
            }

            override fun onInterstitialAdShowSucceeded() {
                Log.d(TAG, "onInterstitialAdShowSucceeded")
            }

            override fun onInterstitialAdShowFailed(p0: IronSourceError?) {
                Log.d(TAG, "onInterstitialAdShowFailed: ${p0!!.errorMessage}")
            }

            override fun onInterstitialAdClicked() {
                Log.d(TAG, "onInterstitialAdClicked")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        IronSource.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        IronSource.onPause(this)
    }

}
