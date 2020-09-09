package com.hms.cookingreceipes

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import com.facebook.ads.AudienceNetworkAds

class CookingApp : Application() {

    private val s = this.javaClass.name

    companion object {
        lateinit var context: Context

        fun getAppVersion(): String {
            var result = ""
            try {
                result = context.packageManager
                    .getPackageInfo(context.packageName, 0)
                    .versionName
                result = result.replace("[a-zA-Z]|-".toRegex(), "")
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return result
        }

        fun getAppVersionNumber(): Int {
            var result = 0
            try {
                result = context.packageManager
                    .getPackageInfo(context.packageName, 0)
                    .versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return result
        }

        fun openMarket(url: String, appPackage: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackage"))
            intent.data = Uri.parse(url)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ContextCompat.startActivity(context, intent, null)
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        AudienceNetworkAds.initialize(this)
    }
}