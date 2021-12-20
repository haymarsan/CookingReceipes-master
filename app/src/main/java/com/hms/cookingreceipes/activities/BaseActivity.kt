package com.hms.cookingreceipes.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.hms.cookingreceipes.AppUpdateDialogFragment
import com.hms.cookingreceipes.CookingApp.Companion.getAppVersionNumber
import com.hms.cookingreceipes.CookingApp.Companion.openLink
import com.hms.cookingreceipes.CookingApp.Companion.openMarket
import com.hms.cookingreceipes.R
import com.hms.cookingreceipes.data.model.AppUpdate

open class BaseActivity : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("app").child("MMRecepies").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val appUpdate = snapshot.getValue(AppUpdate::class.java)
                val currentAppVersion = getAppVersionNumber(this@BaseActivity)
                if (currentAppVersion < appUpdate!!.versionCode) {
                    showUpdateDialog(appUpdate)
                }

            }
        })
    }

    protected fun showUpdateDialog(
        appUpdate: AppUpdate
    ) {
        val updateDialog = AppUpdateDialogFragment.newInstance(
            appUpdate
        )
        updateDialog.show(supportFragmentManager, AppUpdateDialogFragment::class.java.name)
        updateDialog.setListener(object : AppUpdateDialogFragment.ActionListener {
            override fun onPlayStoreClicked() {
                openMarket(this@BaseActivity, appUpdate.playStore)
            }

            override fun onDirectDownloadClicked() {
                openLink(this@BaseActivity, appUpdate.directDownload!!)
            }
        })
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        animateFadeInOut()
    }

    fun animateFadeInOut() {
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out)
    }
}