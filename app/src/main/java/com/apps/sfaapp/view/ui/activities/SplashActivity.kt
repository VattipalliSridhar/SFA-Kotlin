package com.apps.sfaapp.view.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import com.apps.sfaapp.R
import com.apps.sfaapp.view.base.BaseActivity
import com.apps.sfaapp.view.base.SharedPreferConstant
import com.apps.sfaapp.view.ui.navigators.SplashNavigator
import kotlinx.coroutines.delay

class SplashActivity : BaseActivity(), SplashNavigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)

        if (isNetworkAvailable()) {
            lifecycleScope.launchWhenStarted {
                delay(3000)
                try {
                    if (getPreferLogin(SharedPreferConstant.LOGIN_SUCCESS)!! == "1") {
                        openMainActivity()
                    } else {
                        openLoginActivity()
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

        } else {
            showToastMessage("Please Connect Internet.......")
        }
    }

    override fun openMainActivity() {

        startActivity(Intent(applicationContext, MainActivity::class.java))
        finish()

    }

    override fun openLoginActivity() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }
}