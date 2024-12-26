package com.hms.cookingreceipes.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.hms.cookingreceipes.data.model.Entry
import com.hms.cookingreceipes.databinding.ActivityWebPageBinding

class WebPageActivity : AppCompatActivity() {
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
    }


    inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view!!.loadUrl(url!!)
            return true
        }
    }
}
