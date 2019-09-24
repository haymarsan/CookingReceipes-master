package com.hms.cookingreceipes.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.hms.cookingreceipes.R
import com.hms.cookingreceipes.data.model.Entry
import kotlinx.android.synthetic.main.activity_web_page.*
import kotlinx.android.synthetic.main.list_item_blog.*
import java.text.SimpleDateFormat

class WebPageActivity : AppCompatActivity() {

    val TAG = javaClass.simpleName
    lateinit var mAdView: AdView

    companion object {
        fun newInstance(context: Context, entry: Entry): Intent {
            val intent = Intent(context, WebPageActivity::class.java)
            intent.putExtra("entry", entry)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_page)


//        //actionbar
//        val actionbar = supportActionBar
//        //set actionbar title
//        actionbar!!.title = "New Activity"
//        //set back button
//        actionbar.setDisplayHomeAsUpEnabled(true)
//        actionbar.setDisplayHomeAsUpEnabled(true)



        val entry = intent.getSerializableExtra("entry") as Entry

        val html_text = "<HTML><HEAD>" +
                "<link href=\"css/style.css\" rel=\"stylesheet\" type=\"text/css\">" +
                "</HEAD><body><script src=\"js/script.js\"></script>${entry.content.value}" +
                "</body></HTML>"

        wvWebPage.webViewClient = MyBrowser()
        wvWebPage.loadDataWithBaseURL("file:///android_asset/", html_text, "text/html", "utf-8", null)

        tvTitle.text = entry.title.value
        //tvDate.text = SimpleDateFormat("dd MMM, yyyy HH:MM:SS").format(entry.published.value)


        MobileAds.initialize(this, resources.getString(R.string.APP_ID))
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view!!.loadUrl(url)
            return true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
