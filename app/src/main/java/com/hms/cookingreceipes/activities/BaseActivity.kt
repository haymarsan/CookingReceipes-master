package com.hms.cookingreceipes.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

open class BaseActivity : AppCompatActivity() {

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun openMarket(context: Context, appUrl: String) {
        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${this.packageName}"))
        intent.data = Uri.parse(appUrl)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        ContextCompat.startActivity(context, intent, null)
    }

    protected fun getAppVersion(): String {
        var result = ""
        try {
            result = this.packageManager
                .getPackageInfo(this.packageName, 0)
                .versionName.toString()
            result = result.replace("[a-zA-Z]|-".toRegex(), "")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return result
    }
}