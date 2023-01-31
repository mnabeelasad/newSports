package com.go.sport.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.helper.MKeyHelper
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.walkthrough.WalkThroughActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        MKeyHelper.get(this, "SHA1")
        MKeyHelper.printHashKey(this)

        fullScreen()

        iv_logo.visibility = View.GONE

        Handler().postDelayed({
            iv_logo.visibility = View.VISIBLE
            openNextActivity()
        }, 800)
    }

    private fun openNextActivity() {
        Handler().postDelayed({
            if (MySharedPreference(this).getUserObject() != null)
                startActivity(this, DashboardActivity::class.java, true, 4)
            else
                startActivity(this, WalkThroughActivity::class.java, true, 4)
        }, 1500)
    }
}