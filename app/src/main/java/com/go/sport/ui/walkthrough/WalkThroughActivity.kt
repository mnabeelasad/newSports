package com.go.sport.ui.walkthrough

import android.annotation.SuppressLint
import android.os.Bundle
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.ui.signin.SignInFirstActivity
import com.go.sport.ui.signup.SignUpActivity
import com.go.sport.ui.walkthrough.adapter.WalkThroughPagerAdapter
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_walk_through.*
import java.util.concurrent.TimeUnit

class WalkThroughActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walk_through)

        initViews()
        spring_dots_indicator.setViewPager(view_pager)
        initListeners()

    }
    override fun onBackPressed() {
        finishAffinity()
    }

    private fun initViews() {
        view_pager.adapter = WalkThroughPagerAdapter(supportFragmentManager)
        view_pager.setPageTransformer(false) { view, position ->
            // Ensures the views overlap each other.
            view.translationX = view.width * -position
            // Alpha property is based on the view position.
            if (position <= -1.0F || position >= 1.0F) {
                view.alpha = 0.0F
            } else if (position == 0.0F) {
                view.alpha = 1.0F
            } else { // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                view.alpha = 1.0F - kotlin.math.abs(position)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(cont_login).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivity(this, SignInFirstActivity::class.java, false, 1)
        }

        RxView.clicks(cont_join).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivity(this, SignUpActivity::class.java, false, 1)
        }
    }


}