package com.hms.cookingreceipes.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.hms.cookingreceipes.R
import com.hms.cookingreceipes.data.model.Entry
import com.hms.cookingreceipes.databinding.ActivityWebPageBinding
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSourceBannerLayout
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.BannerListener

class WebPageActivity : AppCompatActivity() {

    val TAG = javaClass.simpleName
    private lateinit var binding: ActivityWebPageBinding

    companion object {
        fun newInstance(context: Context, entry: Entry): Intent {
            val intent = Intent(context, WebPageActivity::class.java)
            intent.putExtra("entry", entry)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val entry = intent.getSerializableExtra("entry") as Entry

        val html_text = "<HTML><HEAD>" +
                "<link href=\"css/style.css\" rel=\"stylesheet\" type=\"text/css\">" +
                "</HEAD><body><script src=\"js/script.js\"></script>${entry.content.value}" +
                "</body></HTML>"

        binding.wvWebPage.webViewClient = MyBrowser()
        binding.wvWebPage.loadDataWithBaseURL(
            "file:///android_asset/",
            html_text,
            "text/html",
            "utf-8",
            null
        )

        binding.tvTitle.text = entry.title.value

        createAndloadBanner()

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


    inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view!!.loadUrl(url!!)
            return true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
