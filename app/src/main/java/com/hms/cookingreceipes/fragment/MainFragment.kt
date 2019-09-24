package com.hms.cookingreceipes.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.hms.cookingreceipes.R
import com.hms.cookingreceipes.activities.WebPageActivity
import com.hms.cookingreceipes.adapter.BlogspotAdapter
import com.hms.cookingreceipes.data.model.Entry
import com.hms.cookingreceipes.utils.AppUtils
import com.hms.cookingreceipes.utils.PreferenceUtils
import com.hms.cookingreceipes.viewmodel.CookingAppViewModel


class MainFragment : Fragment() {

    val TAG = javaClass.simpleName
    lateinit var mAppViewModel: CookingAppViewModel
    lateinit var mainRecyclerView: RecyclerView
    lateinit var mainProgress: ProgressBar
    lateinit var nestedScrollView: NestedScrollView
    private val blogspotAdapter = BlogspotAdapter()

    private var isLoading: Boolean = false

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAppViewModel = ViewModelProviders.of(this).get(CookingAppViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        mainRecyclerView = view.findViewById(R.id.recyclerMain)
        mainProgress = view.findViewById(R.id.mainProgress)
        nestedScrollView = view.findViewById(R.id.viewNestedScroll)

        mainRecyclerView.layoutManager = LinearLayoutManager(this.context)
        mainRecyclerView.recycledViewPool.setMaxRecycledViews(0, 0)
        mainRecyclerView.adapter = blogspotAdapter

        onLoadMoreData(1, 5)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (mainRecyclerView.visibility == View.VISIBLE) {
                if (v.getChildAt(v.childCount - 1) != null) {
                    if ((scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight) &&
                        scrollY > oldScrollY
                    ) {
                        if (!isLoading) {
                            onLoadMoreData(
                                PreferenceUtils(context!!).loadEndPage() + 1,
                                PreferenceUtils(context!!).loadEndPage() + 5
                            )
                        }
                    }
                }
            }
        }

        blogspotAdapter.setOnItemClickListener(object : BlogspotAdapter.OnItemClickListener {
            override fun onItemClick(entry: Entry) {
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.")
                }
                startActivity(WebPageActivity.newInstance(this@MainFragment.context!!, entry))
            }

        })
    }

    override fun onResume() {
        super.onResume()
        MobileAds.initialize(context, getString(R.string.APP_ID))
        mInterstitialAd = InterstitialAd(context)
        mInterstitialAd.adUnitId = getString(R.string.adUnitIdIntersitial1)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    private fun onLoadMoreData(start: Int, end: Int) {
        if (AppUtils().hasConnection(context!!)) {
            mainProgress.visibility = View.VISIBLE
            val params = HashMap<String, String>()
            params.put("alt", "json")
            params.put("start-index", "$start")
            params.put("max-results", "$end")

            mAppViewModel.getBlogArticles(params).observe(this, Observer {
                if(it.feed.entry!=null){
                    blogspotAdapter.entryList += it.feed.entry
                    mainProgress.visibility = View.GONE
                    PreferenceUtils(context!!).saveStartPage(start)
                    PreferenceUtils(context!!).saveEndPage(end)
                }
                else{
                    Toast.makeText(activity!!.applicationContext, "No More contents", Toast.LENGTH_LONG).show()
                }
            })
        } else {
            Toast.makeText(activity!!.applicationContext, "No internet connection", Toast.LENGTH_LONG).show()
        }
    }


}
