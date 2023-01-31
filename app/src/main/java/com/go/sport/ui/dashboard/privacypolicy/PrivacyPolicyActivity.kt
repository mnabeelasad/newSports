package com.go.sport.ui.dashboard.privacypolicy

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.network.APIManager
import com.go.sport.sharedpref.MySharedPreference
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_privacy_policy.*

class PrivacyPolicyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)

        tv_pp.movementMethod = ScrollingMovementMethod()
        initListeners()
        getPrivacyPol()
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }
    }

    private fun getPrivacyPol() {
        pBar(1)
        APIManager.getPrivacyPolicy(returnUserAuthToken()) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@getPrivacyPolicy
            }

            if (result?.status == true)
                tv_pp.text = result.data[0].description
        }
    }
}