package com.hms.cookingreceipes.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.facebook.ads.InterstitialAd
import com.hms.cookingreceipes.CookingApp.Companion.openMarket
import com.hms.cookingreceipes.R
import com.hms.cookingreceipes.adapter.BlogspotAdapter
import com.hms.cookingreceipes.data.model.Entry
import com.hms.cookingreceipes.utils.AppUtils
import com.hms.cookingreceipes.utils.PreferenceUtils
import com.hms.cookingreceipes.viewmodel.CookingAppViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : BaseActivity() {

    private lateinit var mAppViewModel: CookingAppViewModel
    private val blogspotAdapter = BlogspotAdapter()
    private var isLoading: Boolean = false
    //  lateinit var mInterstitialAd: InterstitialAd

    lateinit var mAdView: AdView
    lateinit var mInterstitialAd: InterstitialAd

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mAppViewModel = ViewModelProvider(this).get(CookingAppViewModel::class.java)

        recyclerMain.layoutManager = LinearLayoutManager(this)
        recyclerMain.recycledViewPool.setMaxRecycledViews(0, 0)
        recyclerMain.adapter = blogspotAdapter

        onLoadMoreData(1, 5)

        viewNestedScroll.setOnScrollChangeListener { v: NestedScrollView, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (recyclerMain.visibility == View.VISIBLE) {
                if (v.getChildAt(v.childCount - 1) != null) {
                    if ((scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) &&
                        scrollY > oldScrollY
                    ) {
                        if (!isLoading) {
                            onLoadMoreData(
                                PreferenceUtils(this).loadEndPage() + 1,
                                PreferenceUtils(this).loadEndPage() + 5
                            )
                        }
                    }
                }
            }
        }

        blogspotAdapter.setOnItemClickListener(object : BlogspotAdapter.OnItemClickListener {
            override fun onItemClick(entry: Entry) {
                if (mInterstitialAd.isAdLoaded) {
                    mInterstitialAd.show()
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.")
                }
                startActivity(WebPageActivity.newInstance(this@HomeActivity, entry))
            }

        })
        mAdView = AdView(this, resources.getString(R.string.fb_banner), AdSize.BANNER_HEIGHT_50)
        banner_container1.addView(mAdView)
        mAdView.loadAd()

        mInterstitialAd = InterstitialAd(this, resources.getString(R.string.fb_interstitial))
        mInterstitialAd.loadAd()

    }

    private fun onLoadMoreData(start: Int, end: Int) {
        if (AppUtils().hasConnection(this)) {
            mainProgress.visibility = View.VISIBLE
            val params = HashMap<String, String>()
            params["alt"] = "json"
            params["start-index"] = "$start"
            params["max-results"] = 5.toString()

            mAppViewModel.getBlogArticles(params).observe(this, Observer {
                blogspotAdapter.entryList += it.feed.entry
                mainProgress.visibility = View.GONE
                PreferenceUtils(this).saveStartPage(start)
                PreferenceUtils(this).saveEndPage(end)
            })
        } else {
            showToast("No internet connection")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_update -> openMarket(
                getString(R.string.app_url),
                getString(R.string.app_package)
            )
        }
        return true
    }


    override fun onDestroy() {
        if (mAdView != null) {
            mAdView.destroy()
        }
        if (mInterstitialAd != null) {
            mInterstitialAd.destroy()
        }
        super.onDestroy()

    }

}
