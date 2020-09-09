package com.hms.cookingreceipes.activities

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import com.hms.cookingreceipes.CookingApp.Companion.getAppVersion
import com.hms.cookingreceipes.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    lateinit var animationDrawable: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        tvVersion.text = getString(R.string.version, getAppVersion())

        animationDrawable = ctrLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(1000)
        animationDrawable.setExitFadeDuration(1000)

    }

    override fun onResume() {
        super.onResume()
        if (!animationDrawable.isRunning){
            animationDrawable.start()
            Handler().postDelayed({
                startActivity(HomeActivity.newInstance(this))
                finish()
                animateFadeInOut()
            }, 3000)
        }
    }

    override fun onPause() {
        super.onPause()
        if (animationDrawable.isRunning){
            animationDrawable.stop()
        }
    }

}
