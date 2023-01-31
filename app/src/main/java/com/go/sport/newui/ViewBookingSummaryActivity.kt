package com.go.sport.newui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.extensions.setGlide
import com.go.sport.model.firebase.ChatHeadsModel
import com.go.sport.model.getfeatures.GetFeaturesData
import com.go.sport.model.invites.InvitesInvite
import com.go.sport.model.mygames.Data
import com.go.sport.model.mygames.Feature
import com.go.sport.model.play.PlayData
import com.go.sport.network.APIManager
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.dashboard.chats.ConversationActivity
import com.go.sport.ui.dashboard.fragments.home.HomeFragment
import com.go.sport.ui.dashboard.fragments.home.hostactivity.HostActivityActivity
import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.summary.adapter.VenueFacilitiesAdapter
import com.go.sport.ui.dashboard.fragments.home.mygames.MyGamesActivity
import com.go.sport.ui.dashboard.venueAdapter.AmenetiesAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Single


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
import kotlinx.android.synthetic.main.activity_view_booking_summary.*
import java.util.concurrent.TimeUnit

class ViewBookingSummaryActivity : BaseActivity() {
    private var myGame: Data? = null
    private var invitesInvite: InvitesInvite? = null
    private var playData: PlayData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_booking_summary)
        myGame = Singleton.homeClickedMyGamesModel
        invitesInvite = Singleton.invitesInvite
        playData = Singleton.playData

        initListeners()
        setValues()
    }

    override fun onDestroy() {
        super.onDestroy()
        Singleton.homeClickedMyGamesModel = null
        Singleton.invitesInvite = null
        Singleton.playData = null
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(ic_back).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(back).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            Singleton.isPastGame = false

            startActivityFinishAll(
                this,
                DashboardActivity::class.java,
                true,
                -1
            )
        }

        RxView.clicks(cancel_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            warntoastH("Are you sure you want to delete booking?") {
                CancelBookings(true)
            }
        }
        if (MySharedPreference(this).getUserObject()!!.id.toString() == myGame?.userId.toString()) {
            ic_chat.visibility = View.GONE
        }

        RxView.clicks(ic_chat).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            FirebaseDatabase.getInstance().reference
                .child("Conversation")
                .child(MySharedPreference(this).getUserObject()!!.id.toString())
                .child(myGame?.userId.toString())
                .child("blocked").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.value == "true") {

                            warningToast("You can't send message to this user")
                        } else {
                            val bundle = Bundle()
                            bundle.putParcelable(
                                "userObject", ChatHeadsModel(
                                    myGame?.userId.toString(),
                                    System.currentTimeMillis().toString(),
                                    myGame?.user!!.detail?.profileImage ?: "",
                                    false,
                                    myGame?.user!!.firstName + " " + myGame?.user!!.lastName,
                                    ""
                                )
                            )
                            startActivity(
                                this@ViewBookingSummaryActivity,
                                ConversationActivity::class.java,
                                false,
                                1,
                                bundle
                            )
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }

        val a = myGame?.facility?.lat_lng?.split(",")?.get(0)?.trim()
        val b = myGame?.facility?.lat_lng?.split(",")?.get(1)?.trim()
        RxView.clicks(ic_location).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            val gmmIntentUri = Uri.parse("google.navigation:q=$a,$b")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        RxView.clicks(tv_convert_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            warntoastH("Are you sure you want to convert Booking?") {
                Singleton.from = "book"
                Singleton.selectedTimeSlotStartTime =
                    myGame?.slot?.get(0)?.start.toString()
                Singleton.selectedTimeSlotEndTime = myGame?.slot?.get(0)?.end.toString()

                Singleton.selectedTimeSlotDuration = myGame?.duration.toString()
                Singleton.selectedDate =
                    changeFormatTime("dd-MMM-yyyy", myGame?.date.toString(), "yyyy-MM-dd")
                Singleton.selectedPitch = myGame?.facility?.pitchsize.toString()
                Singleton.selectedSportsName = myGame?.sport?.name.toString()
                Singleton.selectedVenueAddress = myGame?.facility?.address.toString()
                Singleton.sportsImage = myGame?.sport?.image ?: ""
                Singleton.userGender = myGame?.user?.detail?.gender.toString()
                Singleton.gameCost = myGame?.detail?.gameFee.toString()
                Singleton.selectedPitchId = myGame?.detail?.pitchId.toString()
                Singleton.selectedSportsId = myGame?.sportsId.toString()
                Singleton.selectedVenueName = myGame?.facility?.name.toString()
                Singleton.selectedVenueId = myGame?.facility?.id.toString()
                //     Singleton.facilityFeatures = myGame?.facility?.feature.toString()
                Singleton.featuresList.addAll(myGame?.facility!!.feature).toString()
                Singleton.selectedTimeSlots.add(myGame?.detail?.seletedTimeSlots.toString())
//                Singleton.bookingSummaryPayment = myGame?.detail?.paymentType.toString()
                Singleton.VenueLocation =
                    myGame?.facility?.lat_lng?.split(",")?.get(0)?.trim().toString() +
                            "," + myGame?.facility?.lat_lng?.split(",")?.get(1)?.trim().toString()
                Singleton.old_id = myGame?.id!!
                startActivityFinishAll(
                    this, HostActivityActivity::class.java,
                    true,
                    1,
                )
            }
        }
    }

    private fun setValues() {

        if (Singleton.isPastGame) {
            back.visibility = View.VISIBLE
            cancel_activity.visibility = View.GONE
        }
        if (myGame != null) {
            tv_user_name.text =
                myGame?.user?.firstName?.capitalize() + " " + myGame?.user?.lastName?.capitalize()
            if (myGame?.user?.detail != null) {
                Glide.with(this).load(myGame?.user?.detail?.profileImage).into(user_image)
                tv_user_gender.text = myGame?.user?.detail?.gender?.capitalize()
                tv_user_age.text = getAgeFromDOB(
                    myGame?.user?.detail?.dateOfBirth?.split("-")?.get(0)?.toInt() ?: 0,
                    myGame?.user?.detail?.dateOfBirth?.split("-")?.get(1)?.toInt() ?: 0,
                    myGame?.user?.detail?.dateOfBirth?.split("-")?.get(2)?.toInt() ?: 0
                ) + " Years"
            }

            tv_facility_name.text = myGame?.facility?.name?.capitalize()
            date.text = myGame?.date
            ic_sports.setGlide(myGame?.sport?.image ?: "")
            tv_booking_id.text = "Booking ID: " + myGame?.bookingId
            sports_title.text = myGame?.sport?.name?.capitalize()
            tv_location.text = myGame?.facility?.address?.capitalize()
            tv_time_zone.text = myGame?.completeTime.toString()
            tv_pitch_court.text = myGame?.facility?.pitchsize
            tv_duration_time.text = myGame?.duration
            total_cost.text = myGame?.facility?.pricing + myGame?.currency?.capitalize()

            if (myGame?.slot?.get(0)?.paymentType == "cash") {
                radio_seleccted.setImageDrawable(resources.getDrawable(R.drawable.ic_radio_button_selectable))
                radio_un_seleccted.setImageDrawable(resources.getDrawable(R.drawable.ic_radio_unselected))
            } else if (myGame?.slot?.get(0)?.paymentType == "wallet") {
                radio_seleccted.setImageDrawable(resources.getDrawable(R.drawable.ic_radio_unselected))
                radio_un_seleccted.setImageDrawable(resources.getDrawable(R.drawable.ic_radio_button_selectable))
            } else if (myGame?.slot?.get(0)?.paymentType == "card") {
                radio_seleccted.setImageDrawable(resources.getDrawable(R.drawable.ic_radio_unselected))
                radio_un_seleccted.setImageDrawable(resources.getDrawable(R.drawable.ic_radio_button_selectable))
            } else if (myGame?.slot?.get(0)?.paymentType == "both") {
                radio_seleccted.setImageDrawable(resources.getDrawable(R.drawable.ic_radio_button_selectable))
                radio_un_seleccted.setImageDrawable(resources.getDrawable(R.drawable.ic_radio_button_selectable))
            }

            if (myGame?.facility != null) {
                rv_facilities.apply {
                    adapter = AmenetiesAdapter(
                        this@ViewBookingSummaryActivity,
                        myGame?.facility?.feature as ArrayList<GetFeaturesData>
                    )
                }
            }
        }
    }

    private fun CancelBookings(isRefresh: Boolean = false) {
        val game_id = myGame?.id
        pBar(1)
        APIManager.CancelBookings(returnUserAuthToken(), game_id.toString()) { result, error ->
            pBar(0)
            if (isRefresh)
            if (error != null) {
                mOnError(error)
                return@CancelBookings
            } else if (result!!.status == "true") {
                isRefresh
                successToast(result.message)
                onBackPressed()
                //startActivityFinishAll(this, MyGamesActivity::class.java, true, -1)
            }

        }
    }

}