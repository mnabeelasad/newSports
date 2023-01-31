package com.go.sport.ui.dashboard.fragments.home.offers

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.os.bundleOf
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getoffers.Offer
import com.go.sport.network.APIManager
import com.go.sport.singleton.Singleton.academy
import com.go.sport.ui.dashboard.fragments.book.venuedetails.VenueDetailsActivity
import com.go.sport.ui.dashboard.fragments.home.offers.adapter.OffersAdapter
import com.go.sport.ui.dashboard.fragments.home.offers.details.OfferDetailsActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_offers.*

class OffersActivity : BaseActivity(), OffersAdapter.OffersClickListener {

    private var offersList = ArrayList<Offer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offers)

        initViews()
        initListeners()
        getOffers()
    }

    private fun initViews() {
        rv_offers.adapter = OffersAdapter(this, offersList, this)
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }
    }

    private fun getOffers() {
        pBar(1)

        APIManager.getOffers(returnUserAuthToken()) { result, error ->

            pBar(0)

            if (error != null) {
                mOnError(error)
            }

            if (result?.status == "true") {
                offersList.clear()
                offersList.addAll(result.offer)
                rv_offers.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onOffersItemClick(position: Int) {
        val bundle = bundleOf(Pair("id", offersList[position].facilityId))
        //   startActivity(this, OfferDetailsActivity::class.java, false, 1, bundle)
        startActivity(this, VenueDetailsActivity::class.java, false, 1, bundle)
    }

    override fun onFacebookClick(position: Int) {
        val mediaLinks = offersList[position].mediaLinks.split(",")[0]
        openBrowser(mediaLinks)
    }

    override fun onGoogleClick(position: Int) {
        val mediaLinks = offersList[position].mediaLinks.split(",")[1]
        openBrowser(mediaLinks)
    }

    override fun onWhatsappClick(position: Int) {
        openWhatsApp(offersList[position].whatsapp)
    }
}