package com.go.sport.ui.dashboard.termsconditions

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.network.APIManager
import com.go.sport.sharedpref.MySharedPreference
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_terms_and_conditions.*

class TermsAndConditionsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_conditions)

        tv_tnc.movementMethod = ScrollingMovementMethod()
        initListeners()
        getTermsAndCond()
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }
    }

    private fun getTermsAndCond() {
        pBar(1)
        APIManager.getTermsAndConditions(returnUserAuthToken()) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@getTermsAndConditions
            }

            if (result?.status == true)
                tv_tnc.text = result.data[0].description
        }
    }
}