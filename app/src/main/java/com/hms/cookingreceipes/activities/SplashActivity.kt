package com.hms.cookingreceipes.activities

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.hms.cookingreceipes.CookingApp.Companion.getAppVersion
import com.hms.cookingreceipes.R
import com.hms.cookingreceipes.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {

    lateinit var animationDrawable: AnimationDrawable
    private lateinit var binding: ActivitySplashBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvVersion.text = getString(R.string.version, getAppVersion())

        animationDrawable = binding.ctrLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(1000)
        animationDrawable.setExitFadeDuration(1000)

        auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword("admin@myhealth.com", "Admin#12345")
            .addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("Auth Status", "Authentication Success")
                } else {
                    Log.i("Auth Status", "Authentication Fail")
                }
            })
    }

    override fun onResume() {
        super.onResume()
        if (!animationDrawable.isRunning) {
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
        if (animationDrawable.isRunning) {
            animationDrawable.stop()
        }
    }

}
