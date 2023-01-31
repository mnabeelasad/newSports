package com.go.sport.newui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.base.BaseActivity

import com.go.sport.model.getfeatures.GetFeaturesData
import com.go.sport.network.APIManager
import com.go.sport.newui.adapter.VenueBookingFacilityAdapter
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.dashboard.fragments.home.hostactivity.HostActivityActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.*
import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.date
import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.sports_title
import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.total_cost
import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.tv_booking_id
import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.tv_duration_time
import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.tv_facility_name
import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.tv_location

import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.tv_pitch_court
import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.tv_time_zone
import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.tv_user_age
import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.tv_user_gender
import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.tv_user_name
import kotlinx.android.synthetic.main.activity_new_summary_screen.*
import kotlinx.android.synthetic.main.activity_new_summary_screen.ic_back
import kotlinx.android.synthetic.main.activity_new_summary_screen.iv_sports
import kotlinx.android.synthetic.main.activity_new_summary_screen.tv_gender
import kotlinx.android.synthetic.main.activity_new_summary_screen.tv_user
import kotlinx.android.synthetic.main.activity_view_booking_summary.*
import kotlinx.android.synthetic.main.fragment_new_summary_screen.view.*
import java.util.concurrent.TimeUnit

class NewBookingSummaryScreen : BaseActivity() {
//    private val facilities = arrayListOf<GetFacilityDataFeature>()

    var isFinish: Boolean = false
    var gameId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_booking_summary_screen)

        initViews()
        initListeners()
        getData()
    }

    private fun initViews() {
        rv_facilities1.apply {
            adapter = VenueBookingFacilityAdapter(
                this@NewBookingSummaryScreen, Singleton.featuresList
            )
        }
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(ic_back).subscribe {
            if (isFinish == true) {
                startActivityFinishAll(
                    this,
                    DashboardActivity::class.java,
                    true,
                    -1
                )
                onDestroy()
            } else {
                onBackPressed()
            }
        }
        RxView.clicks(create_booking).subscribe {
            bookFacility()
        }

        RxView.clicks(cont_back).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivityFinishAll(
                this,
                DashboardActivity::class.java,
                true,
                -1
            )
        }
        RxView.clicks(btn_back).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivityFinishAll(
                this,
                DashboardActivity::class.java,
                true,
                -1
            )
        }
        val a = Singleton.VenueLocation.split(",")[0].trim()
        val b = Singleton.VenueLocation.split(",")[1].trim()
        RxView.clicks(iv_location).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            val gmmIntentUri = Uri.parse("google.navigation:q=$a,$b")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        RxView.clicks(convert_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivityFinishAll(
                this, HostActivityActivity::class.java,
                true,
                1,
            )
        }
    }


    override fun onBackPressed() {
        if (isFinish == true) {
            startActivityFinishAll(
                this,
                DashboardActivity::class.java,
                true,
                -1
            )
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getData() {
        val name = MySharedPreference(this).getUserObject()?.first_name?.capitalize()
            .toString() + " " + MySharedPreference(
            this
        ).getUserObject()?.last_name?.capitalize().toString()
        tv_user_name.text = name

        tv_user_gender.text = MySharedPreference(this).getUserObject()
            ?.detail?.gender?.capitalize()
        tv_user_age.text = getAgeFromDOB(
            MySharedPreference(this).getUserObject()?.detail?.date_of_birth?.split("-")?.get(0)
                ?.toInt() ?: 0,
            MySharedPreference(this).getUserObject()?.detail?.date_of_birth?.split("-")?.get(1)
                ?.toInt() ?: 0,
            MySharedPreference(this).getUserObject()?.detail?.date_of_birth?.split("-")?.get(2)
                ?.toInt() ?: 0
        ) + " Years"
        Glide.with(this).load(
            MySharedPreference(this)
                .getUserObject()?.detail?.profile_image
        ).into(iv_user_image)
        Glide.with(this).load(Singleton.sportsImage).into(iv_sports)
        tv_booking_id.visibility = View.GONE
        sports_title.text = Singleton.selectedSportsName?.capitalize()
        tv_facility_name.text = Singleton.selectedVenueName?.capitalize()
        tv_location.text = Singleton.selectedVenueAddress?.capitalize()
        date.text = Singleton.selectedDate
        tv_time_zone.text =
            changeFormatTime("hh:mm:ss", Singleton.selectedTimeSlotStartTime, "hh:mm a")+ " - " +
                    changeFormatTime("hh:mm:ss", Singleton.selectedTimeSlotEndTime, "hh:mm a")
        tv_pitch_court.text = Singleton.selectedPitch
        tv_duration_time.text = Singleton.selectedTimeSlotDuration + " Minutes"
        total_cost.text = "PKR ${Singleton.gameCost}"


        if (Singleton.bookingSummaryPayment == "cash") {
            radio_selected.setImageResource(R.drawable.ic_radio_button_selectable)
            radio_un_selected.setImageResource(R.drawable.ic_radio_unselected)
        } else if (Singleton.bookingSummaryPayment == "wallet") {
            radio_selected.setImageResource(R.drawable.ic_radio_unselected)
            radio_un_selected.setImageResource(R.drawable.ic_radio_button_selectable)
        } else if (Singleton.bookingSummaryPayment == "card") {
            radio_selected.setImageResource(R.drawable.ic_radio_unselected)
            radio_un_selected.setImageResource(R.drawable.ic_radio_button_selectable)
        } else if (Singleton.bookingSummaryPayment == "cash" && Singleton.bookingSummaryPayment == "wallet"
            || Singleton.bookingSummaryPayment == "card"
        ) {
            radio_selected.setImageResource(R.drawable.ic_radio_button_selectable)
            radio_un_selected.setImageResource(R.drawable.ic_radio_button_selectable)
        }
    }


    @SuppressLint("CheckResult")
    private fun bookFacility() {
        pBar(1)

        APIManager.bookFacility(
            returnUserAuthToken(),
            Singleton.selectedSportsName,
            Singleton.selectedSportsId,
            Singleton.selectedDate,
            Singleton.selectedVenueId,
            Singleton.selectedPitchId,
            Singleton.gameCost,
            Singleton.selectedTimeSlots.joinToString { it },
            Singleton.selectedVenueAddress,
            Singleton.bookingSummaryPayment,
            Singleton.featuresList.toString(),
            Singleton.VenueLocation,
//            Singleton.bookingID,
        ) { result, error ->

            pBar(0)

            if (error != null) {
                mOnError(error)
                return@bookFacility
            }

            if (result?.status == true) {
                gameId = result.game.id
                if (result.message == "Facility Booked.") {
                    successToast("Booked Successfully")
                    isFinish = true
                }

                create_booking.visibility = View.GONE
                cont_back.visibility = View.GONE
                convert_activity.visibility = View.VISIBLE
                btn_back.visibility = View.VISIBLE
                btn_cancel.visibility = View.VISIBLE
                tv_booking_id.text = "Booking Id: " + result.game.booking_id
                tv_booking_id.visibility = View.VISIBLE


                RxView.clicks(btn_cancel).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                    CancelBookings()
                }
                RxView.clicks(convert_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {

                    warntoastH("Are you sure you want to convert Booking?") {
                        Singleton.from = "book"
                        Singleton.selectedTimeSlotStartTime = changeFormatTime("hh:mm:ss",  result.game.slot[0].start, "hh:mm a")

                        Singleton.selectedTimeSlotEndTime =  result.game.slot[0].end
                        //    Singleton.selectedTimeSlotDuration = result.game.detail
                        Singleton.sportsImage = result.game.sport.image
                        Singleton.selectedDate = result.game.date
                        Singleton.selectedPitch = result.game.facility.pitchsize
                        Singleton.selectedSportsName = result.game.title
                        Singleton.selectedVenueAddress = result.game.facility.address
                        Singleton.sportsImage = result.game.image
                        Singleton.gameCost = result.game.detail.game_fee
                        Singleton.selectedPitchId = result.game.detail.pitch_id
                        Singleton.selectedSportsId = result.game.sports_id
                        Singleton.selectedVenueName = result.game.facility.name
                        Singleton.selectedVenueId = result.game.facility.id.toString()
                        Singleton.featuresList = result.game.facility.feature as ArrayList<GetFeaturesData>
                        Singleton.selectedTimeSlots.add(result.game.detail.seleted_time_slots)
                        Singleton.bookingSummaryPayment = result.game.detail.payment_type
                        Singleton.VenueLocation = result.game.lat_lng.split(",")[0].trim() +
                                "," + result.game.lat_lng.split(",")[1].trim()
                        Singleton.old_id = result.game.id
                        startActivityFinishAll(
                            this, HostActivityActivity::class.java,
                            true,
                            1,
                        )

                    }
                }


            }
        }

    }


    private fun CancelBookings() {

        pBar(1)
        APIManager.CancelBookings(returnUserAuthToken(), gameId.toString()) { result, error ->
            pBar(0)
            if (error != null) {
                mOnError(error)
                return@CancelBookings
            } else if (result!!.status == "true") {
                successToast(result.message)
                onBackPressed()
            }

        }
    }


}