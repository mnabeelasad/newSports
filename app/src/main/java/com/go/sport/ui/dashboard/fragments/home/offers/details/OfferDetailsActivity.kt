package com.go.sport.ui.dashboard.fragments.home.offers.details

import android.annotation.SuppressLint
import android.os.Bundle
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.ui.dashboard.fragments.book.venuedetails.VenueDetailsActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_offer_details.*
import kotlinx.android.synthetic.main.activity_offers.iv_back

class OfferDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_details)

        initListeners()
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(cont_avail_offer).subscribe {
            startActivity(this, VenueDetailsActivity::class.java, false, 1)
        }
    }
}