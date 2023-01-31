package com.go.sport.ui.dashboard.fragments.book.venuedetails

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getfeatures.GetFeaturesData
import com.go.sport.network.APIManager
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.fragments.book.adapter.VenueFacilityAdapter
import com.go.sport.ui.dashboard.fragments.book.sports.BookNowSportsActivity
import com.go.sport.ui.dashboard.fragments.book.venuedetails.adapter.GroundAmenitiesAdapter
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_venue_details.*
import kotlinx.android.synthetic.main.fragment_select_date.view.*
import java.util.concurrent.TimeUnit

class VenueDetailsActivity : BaseActivity() {

    private val amenities = arrayListOf<GetFeaturesData>()

    private var id = ""
    private var address = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_details)

        id = intent?.extras?.getString("id") ?: ""
        address = intent?.extras?.getString("address") ?: ""
        Singleton.selectedVenueAddress = address


        initViews()
        initListeners()
        mGetFacilityDetails()
    }

    private fun initViews() {
        rv_amenities.apply {
            adapter = GroundAmenitiesAdapter(this@VenueDetailsActivity, amenities)
        }

        rv_timing.apply {
            adapter = VenueFacilityAdapter(this@VenueDetailsActivity)
        }

        tv_desc.text = address
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(tv_book_now).throttleFirst(2, TimeUnit.SECONDS).subscribe {

            Singleton.selectedVenueAddress = address


            startActivity(
                this,
                BookNowSportsActivity::class.java,
                false,
                1,
                bundleOf(Pair("id", id), Pair("address", address))
            )
        }

        RxView.clicks(nested_twitter).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            openTwitterApp(tv_twitter.text.toString())
        }

        RxView.clicks(nested_facebook).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            openFacebookApp(tv_facebook.text.toString())
        }

        RxView.clicks(nested_insta).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            openInstagramApp()
        }

    }

    @SuppressLint("CheckResult")
    private fun mGetFacilityDetails() {
        pBar(1)
        APIManager.getFacilityData(
            returnUserAuthToken(),
            id
        ) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@getFacilityData
            }

            if (result != null) {
                val circularProgressDrawable = CircularProgressDrawable(this)
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                Glide.with(this).load(result.facility.image).placeholder(circularProgressDrawable)
                    .into(header_back)

                tv_title.text = result.facility.name
                tv_rating.text = result.facility.rating
                tv_price.text = "PKR " + result.facility.pricing
                tv_contact_num.text = result.facility.phone
                tv_facebook.text = result.facility.socialIds.split(",")[2]
                tv_twitter.text = result.facility.socialIds.split(",")[1]
                tv_insta.text = result.facility.socialIds.split(",")[0]
                simple_rating_bar.rating = result.facility.rating.toFloat()

                var a = result.facility.latLng.split(",")[0].trim()
                var b = result.facility.latLng.split(",")[1].trim()
                RxView.clicks(iv_direction).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                    val gmmIntentUri = Uri.parse("google.navigation:q=$a,$b")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)
                }
                Singleton.gameCost = result.facility.pricing

                if (!result.facility.feature.isNullOrEmpty()) {
                    amenities.clear()
                    amenities.addAll(result.facility.feature)
                    Singleton.selectedFacilities.clear()
                    amenities.map {
                        Singleton.selectedFacilities.add(
                            GetFeaturesData(
                                it.created_at,
                                it.id.toString(),
                                it.image,
                                it.name,
                                it.status,
                                false,
                                it.updated_at,
                            )
                        )
                    }
                }

                rv_amenities.adapter?.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openTwitterApp(shareText: String) {
        val uri = Uri.parse("http://twitter.com/")
        val shareIntent = Intent(Intent.ACTION_VIEW, uri)
        shareIntent.setPackage("com.twitter.android")
        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://twitter.com/")
                )
            )
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openFacebookApp(shareText: String) {
        val uri = Uri.parse("http://facebook.com/")
        val shareIntent = Intent(Intent.ACTION_VIEW, uri)
        shareIntent.setPackage("com.facebook.katana")
        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://facebook.com/")
                )
            )
        }
    }

    private fun openInstagramApp() {
        val uri = Uri.parse("http://instagram.com/")
        val shareIntent = Intent(Intent.ACTION_VIEW, uri)
        shareIntent.setPackage("com.instagram.android")
        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/")
                )
            )
        }
    }
}


