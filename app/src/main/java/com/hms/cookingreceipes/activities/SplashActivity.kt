package com.hms.cookingreceipes.activities

import android.annotation.SuppressLint
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.hms.cookingreceipes.R
import com.hms.cookingreceipes.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private lateinit var animationDrawable: AnimationDrawable
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvVersion.text = getString(R.string.version, getAppVersion())

        animationDrawable = binding.ctrLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(1000)
        animationDrawable.setExitFadeDuration(1000)

        if (!animationDrawable.isRunning) {
            animationDrawable.start()
            lifecycleScope.launch {
                delay(2000)
                finish()
                startActivity(HomeActivity.newInstance(this@SplashActivity))
                overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (animationDrawable.isRunning) {
            animationDrawable.stop()
        }
    }

}
