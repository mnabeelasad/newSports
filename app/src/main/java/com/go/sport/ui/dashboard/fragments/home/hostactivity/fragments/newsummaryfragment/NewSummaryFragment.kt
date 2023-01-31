package com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.newsummaryfragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.hostactivity.hostForLocation.HostActivityForLocationGame
import com.go.sport.model.hostactivity.hostForLocation.HostActivityForLocationModel
import com.go.sport.model.mygames.Data
import com.go.sport.network.APIManager
import com.go.sport.newui.adapter.VenueBookingFacilityAdapter
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.dashboard.fragments.home.HomeFragment
import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.price.PricingFragment
import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.summary.adapter.VenueFacilitiesAdapter
import com.go.sport.ui.dashboard.fragments.home.mygames.MyGamesActivity
import com.go.sport.ui.dashboard.venueAdapter.AmenetiesAdapter
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_view_booking_summary.*
import kotlinx.android.synthetic.main.fragment_new_summary_screen.view.*
import java.util.*
import java.util.concurrent.TimeUnit

class NewSummaryFragment : Fragment() {

    private lateinit var rootView: View
    var gameId = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_new_summary_screen, container, false)

        initViews()
        setValues()
        initListeners()


        return rootView
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(rootView.summary_create_activity).throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                if (Singleton.from == "location" && Singleton.eventType == "public") {
                    hostActivityForLocation()
                } else if (Singleton.from == "location" && Singleton.eventType == "invites") {
                    hostActivityForLocationInvite()
                } else if (Singleton.from == "venue" && Singleton.eventType == "invites") {
                    hostActivityForInvite()
                } else if (Singleton.from == "book" && Singleton.eventType == "invites") {
                    hostActivityForInvite()
                } else {
                    hostActivity()
                }
            }

        RxView.clicks(rootView.summary_cancel_activity).throttleFirst(2, TimeUnit.SECONDS)
            .subscribe {
                onDestroy()
                (requireActivity() as BaseActivity).startActivityFinishAll(
                    requireContext(),
                    DashboardActivity::class.java,
                    true,
                    -1
                )
            }
        RxView.clicks(rootView.summary_back).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (requireActivity() as BaseActivity).startActivityFinishAll(
                requireContext(),
                DashboardActivity::class.java,
                true,
                -1
            )
        }

        RxView.clicks(rootView.iv_location).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (Singleton.from == "location") {
                val a = Singleton.latLngFromLocation.split(",")[0].trim()
                val b = Singleton.latLngFromLocation.split(",")[1].trim()
                val gmmIntentUri = Uri.parse("google.navigation:q=$a,$b")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            } else if (Singleton.from == "venue") {
                val a = Singleton.VenueLocation.split(",")[0].trim()
                val b = Singleton.VenueLocation.split(",")[1].trim()
                val gmmIntentUri = Uri.parse("google.navigation:q=$a,$b")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            } else if (Singleton.from == "book") {
                val gmmIntentUri =
                    Uri.parse("google.navigation:q=${Singleton.latitude},${Singleton.longitude}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }

    private fun initViews() {
        if (Singleton.from == "location") {
            rootView.rv_booking_facilities.visibility = View.GONE
            rootView.rv_summary_facilities.visibility = View.VISIBLE
            rootView.rv_summary_facilities.apply {
                adapter = AmenetiesAdapter(requireActivity(), Singleton.selectedFacilities)
            }
        } else if (Singleton.from == "book" || Singleton.from == "venue") {
            rootView.rv_booking_facilities.visibility = View.VISIBLE
            rootView.rv_summary_facilities.visibility = View.GONE
            rootView.rv_booking_facilities.apply {
                adapter = VenueBookingFacilityAdapter(requireContext(), Singleton.featuresList)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setValues() {

        if (Singleton.from == "location") {
            rootView.summary_back.visibility = View.GONE
            rootView.game_cost.visibility = View.GONE
            rootView.player_cost.visibility = View.VISIBLE
            rootView.ic_slot.visibility = View.GONE
            rootView.extra_view.visibility = View.GONE
            rootView.tv_summary_user.text =
                MySharedPreference(requireContext()).getUserObject()?.first_name?.capitalize() + " " + MySharedPreference(
                    requireContext()
                ).getUserObject()?.last_name?.capitalize()
            rootView.tv_summary_user_gender.text =
                MySharedPreference(requireContext()).getUserObject()
                    ?.detail?.gender?.capitalize()
            var year =
                MySharedPreference(requireContext()).getUserObject()?.detail?.date_of_birth?.split("-")
                    ?.get(0)
                    ?.toInt()
            var month =
                MySharedPreference(requireContext()).getUserObject()?.detail?.date_of_birth?.split("-")
                    ?.get(1)
                    ?.toInt()
            var date =
                MySharedPreference(requireContext()).getUserObject()?.detail?.date_of_birth?.split("-")
                    ?.get(2)
                    ?.toInt()

            rootView.tv_summary_age.text =
                year?.let {
                    month?.let { month ->
                        date?.let { date ->
                            (requireContext() as BaseActivity).getAgeFromDOB(
                                year,
                                month, date
                            ) + " Years"
                        }
                    }
                }
            Glide.with(this).load(Singleton.sportsImage).into(rootView.summary_iv_sports)
            Glide.with(this).load(
                MySharedPreference(requireContext())
                    .getUserObject()?.detail?.profile_image
            ).into(rootView.ic_image)
            rootView.tv_summary_sports_title.text = Singleton.selectedSportsName?.capitalize()
            rootView.tv_summary_title.text = Singleton.selectedVenueAddress?.capitalize()
            rootView.tv_summary_pitch.text = Singleton.selectedPitch
            rootView.summary_date_view.text = Singleton.selectedDate
            rootView.summary_time_zone.text =
                Singleton.selectedTimeSlotStartTime + " - " + Singleton.selectedTimeSlotEndTime
            rootView.summary_age_view.text = Singleton.ageMin + "-" + Singleton.ageMax
            rootView.summary_skill_set.text = Singleton.skillLevel?.capitalize()
            rootView.total_summary_players_view.text = Singleton.totalPlayers
            rootView.confrm_summary_players.text = Singleton.confirmedPlayers
            rootView.player_summary_cost.text = "PKR " + Singleton.costPerPlay
            rootView.payment_summary_type.text = Singleton.pricingType?.capitalize()
            if (Singleton.additionalInfo != "none") {
                rootView.tv_additional.visibility = View.VISIBLE
                rootView.extra.visibility = View.VISIBLE
                rootView.tv_add_summary_details.text = Singleton.additionalInfo?.capitalize()
            } else {
                rootView.tv_additional.visibility = View.GONE
                rootView.extra.visibility = View.GONE
            }
            rootView.rv_summary_facilities.adapter?.notifyDataSetChanged()

        }
        else if (Singleton.from == "venue") {
            rootView.summary_back.visibility = View.GONE
            rootView.tv_summary_user.text =
                MySharedPreference(requireContext()).getUserObject()?.first_name?.capitalize() + " " + MySharedPreference(
                    requireContext()
                ).getUserObject()?.last_name?.capitalize()
            rootView.tv_summary_user_gender.text =
                MySharedPreference(requireContext()).getUserObject()
                    ?.detail?.gender?.capitalize()
            var year =
                MySharedPreference(requireContext()).getUserObject()?.detail?.date_of_birth?.split("-")
                    ?.get(0)
                    ?.toInt()
            var month =
                MySharedPreference(requireContext()).getUserObject()?.detail?.date_of_birth?.split("-")
                    ?.get(1)
                    ?.toInt()
            var date =
                MySharedPreference(requireContext()).getUserObject()?.detail?.date_of_birth?.split("-")
                    ?.get(2)
                    ?.toInt()

            rootView.tv_summary_age.text =
                year?.let {
                    month?.let { month ->
                        date?.let { date ->
                            (requireContext() as BaseActivity).getAgeFromDOB(
                                year,
                                month, date
                            ) + " Years"
                        }
                    }
                }
            Glide.with(this).load(Singleton.sportsImage).into(rootView.summary_iv_sports)
            Glide.with(this).load(
                MySharedPreference(requireContext())
                    .getUserObject()?.detail?.profile_image
            ).into(rootView.ic_image)
            rootView.tv_summary_sports_title.text = Singleton.selectedSportsName.capitalize()
            rootView.tv_summary_title.text = Singleton.selectedVenueAddress.capitalize()
            rootView.tv_summary_pitch.text = Singleton.selectedPitch
            rootView.summary_date_view.text = Singleton.selectedDate
            rootView.summary_time_zone.text =
                Singleton.selectedTimeSlotStartTime + " - " + Singleton.selectedTimeSlotEndTime
            rootView.summary_age_view.text = Singleton.ageMin + "-" + Singleton.ageMax
            rootView.summary_skill_set.text = Singleton.skillLevel.capitalize()
            rootView.total_summary_players_view.text = Singleton.totalPlayers
            rootView.confrm_summary_players.text = Singleton.confirmedPlayers
            rootView.game_summary_cost.text = "PKR " + Singleton.gameCost
            rootView.player_summary_cost.text = "PKR " + Singleton.costPerPlay
            rootView.payment_summary_type.text =  Singleton.pricingType.capitalize()

            if (Singleton.additionalInfo != "none") {
                rootView.tv_additional.visibility = View.VISIBLE
                rootView.extra.visibility = View.VISIBLE
                rootView.tv_add_summary_details.text = Singleton.additionalInfo?.capitalize()
            } else {
                rootView.tv_additional.visibility = View.GONE
                rootView.extra.visibility = View.GONE
            }
            rootView.rv_booking_facilities.adapter?.notifyDataSetChanged()
        }
        else if (Singleton.from == "book") {
            rootView.summary_back.visibility = View.GONE
            rootView.game_cost.visibility = View.VISIBLE
            rootView.player_cost.visibility = View.VISIBLE
            rootView.ic_slot.visibility = View.VISIBLE
            rootView.extra_view.visibility = View.VISIBLE
            rootView.tv_summary_user.text =
                MySharedPreference(requireContext()).getUserObject()?.first_name?.capitalize() + " " + MySharedPreference(
                    requireContext()
                ).getUserObject()?.last_name?.capitalize()
            rootView.tv_summary_user_gender.text =
                MySharedPreference(requireContext()).getUserObject()?.detail?.gender?.capitalize()
            var year =
                MySharedPreference(requireContext()).getUserObject()?.detail?.date_of_birth?.split("-")
                    ?.get(0)
                    ?.toInt()
            var month =
                MySharedPreference(requireContext()).getUserObject()?.detail?.date_of_birth?.split("-")
                    ?.get(1)
                    ?.toInt()
            var date =
                MySharedPreference(requireContext()).getUserObject()?.detail?.date_of_birth?.split("-")
                    ?.get(2)
                    ?.toInt()

            rootView.tv_summary_age.text =
                year?.let {
                    month?.let { month ->
                        date?.let { date ->
                            (requireContext() as BaseActivity).getAgeFromDOB(
                                year,
                                month, date
                            ) + " Years"
                        }
                    }
                }
            Glide.with(this).load(Singleton.sportsImage).into(rootView.summary_iv_sports)
            Glide.with(this).load(
                MySharedPreference(requireContext())
                    .getUserObject()?.detail?.profile_image
            ).into(rootView.ic_image)
            rootView.tv_summary_sports_title.text = Singleton.selectedSportsName?.capitalize()
            rootView.tv_summary_title.text = Singleton.selectedVenueAddress?.capitalize()
            rootView.tv_summary_pitch.text = Singleton.selectedPitch
            rootView.summary_date_view.text = Singleton.selectedDate
            rootView.summary_time_zone.text =
                Singleton.selectedTimeSlotStartTime + " - " + Singleton.selectedTimeSlotEndTime
            rootView.summary_age_view.text = Singleton.ageMin + " - " + Singleton.ageMax
            rootView.summary_skill_set.text = Singleton.skillLevel.capitalize()
            rootView.total_summary_players_view.text = Singleton.totalPlayers
            rootView.confrm_summary_players.text = Singleton.confirmedPlayers
            rootView.player_summary_cost.text = "PKR " + Singleton.costPerPlay
            rootView.payment_summary_type.text =  Singleton.pricingType.capitalize()

            if (Singleton.additionalInfo != "none") {
                rootView.tv_additional.visibility = View.VISIBLE
                rootView.extra.visibility = View.VISIBLE
                rootView.tv_add_summary_details.text = Singleton.additionalInfo?.capitalize()
            } else {
                rootView.tv_additional.visibility = View.GONE
                rootView.extra.visibility = View.GONE
            }
            rootView.game_summary_cost.text = "PKR " + Singleton.gameCost
            rootView.rv_booking_facilities.adapter?.notifyDataSetChanged()
        }
    }


    private fun hostActivity() {
        (requireActivity() as BaseActivity).pBar(1)
        if (Singleton.featuresList.isNotEmpty()) {
            Singleton.selectedFacilitiesId.clear()
            Singleton.selectedFacilitiesId.addAll(Singleton.featuresList.map { it.id.toString() })
        }
        if (Singleton.old_id == -1) {
            APIManager.hostActivity(
                (requireActivity() as BaseActivity).returnUserAuthToken(),
                Singleton.selectedSportsName,
                Singleton.selectedSportsId,
                Singleton.selectedVenueName,
                Singleton.selectedFacilitiesId.joinToString { it },
                Singleton.additionalInfo,
                Singleton.skillLevel,
                Singleton.totalPlayers,
                Singleton.confirmedPlayers,
                Singleton.gender,
                Singleton.eventType,
                Singleton.costPerPlay,
                Singleton.pricingType,
                Singleton.selectedTimeSlots.joinToString { it },
                Singleton.selectedDate,
                Singleton.selectedVenueId,
                Singleton.ageMin,
                Singleton.ageMax,
                Singleton.selectedVenueAddress,
                Singleton.selectedPitchId,
                Singleton.gameCost,
                Singleton.VenueLocation

            ) { result, error ->
                (requireActivity() as BaseActivity).pBar(0)

                if (error != null) {
                    (requireActivity() as BaseActivity).mOnError(error)
                    return@hostActivity
                }

                if (result?.status == true) {
                    gameId = result.game.id
                    if (result.message == "Activity_created") {
                        (requireActivity() as BaseActivity).successToast("Activity Created")
                    }
                    rootView.summary_back.visibility = View.VISIBLE
                    rootView.delete.visibility = View.VISIBLE
                    rootView.summary_create_activity.visibility = View.GONE
                    rootView.summary_cancel_activity.visibility = View.GONE
                    RxView.clicks(rootView.delete).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                        (requireActivity() as BaseActivity).warntoastH("Are you sure you want to delete Activity") {
                            Singleton.old_id = gameId
                            CancelBookings()
                        }
                    }
                    if (result.message == "Activity_created") {
                        Singleton.isFinish = true
                    }
                }
            }
        } else {
            APIManager.hostActivityConversion(
                (requireActivity() as BaseActivity).returnUserAuthToken(),
                Singleton.selectedSportsName,
                Singleton.selectedSportsId,
                Singleton.selectedVenueName,
                Singleton.selectedFacilitiesId.joinToString { it },
                Singleton.additionalInfo,
                Singleton.skillLevel,
                Singleton.totalPlayers,
                Singleton.confirmedPlayers,
                Singleton.gender,
                Singleton.eventType,
                Singleton.costPerPlay,
                Singleton.pricingType,
                Singleton.selectedTimeSlots.joinToString { it },
                Singleton.selectedDate,
                Singleton.selectedVenueId,
                Singleton.ageMin,
                Singleton.ageMax,
                Singleton.selectedVenueAddress,
                Singleton.selectedPitchId,
                Singleton.gameCost,
                Singleton.old_id,
                Singleton.VenueLocation,
            ) { result, error ->
                (requireActivity() as BaseActivity).pBar(0)

                if (error != null) {
                    (requireActivity() as BaseActivity).mOnError(error)
                    return@hostActivityConversion
                }

                if (result?.status == true) {
                    (requireActivity() as BaseActivity).successToast(result.message)
                    Singleton.isFinish = true
                    rootView.summary_back.visibility = View.VISIBLE
                    rootView.delete.visibility = View.VISIBLE
                    rootView.summary_create_activity.visibility = View.GONE
                    rootView.summary_cancel_activity.visibility = View.GONE

                    RxView.clicks(rootView.delete).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                        (requireActivity() as BaseActivity).warntoastH("Are you sure you want to delete Activity") {
                            gameId = Singleton.old_id
                            cancelConversion()
                        }
                    }

                    if (result.message == "Event Canceled") {
                        Singleton.isFinish = true
                    }
//                startActivityFinishAll(this, DashboardActivity::class.java, true, 1)
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun hostActivityForLocation() {
        (requireActivity() as BaseActivity).pBar(1)
        APIManager.hostActivitylocation(
            (requireActivity() as BaseActivity).returnUserAuthToken(),
            Singleton.selectedSportsName,
            Singleton.selectedSportsId,
            Singleton.skillLevel,
            Singleton.totalPlayers,
            Singleton.confirmedPlayers,
            Singleton.gender,
            Singleton.eventType,
            Singleton.costPerPlay,
            Singleton.pricingType,
            Singleton.selectedDate,
            "0",
            Singleton.ageMin,
            Singleton.ageMax,
            Singleton.selectedVenueAddress,
            Singleton.selectedPitch,
            Singleton.gameCost,
            Singleton.selectedTimeSlotStartTime,
            Singleton.selectedTimeSlotEndTime,
            Singleton.selectedPitch,
            Singleton.selectedFacilitiesId.joinToString { it },
            Singleton.additionalInfo,
            Singleton.latLngFromLocation

        ) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)

            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@hostActivitylocation
            }

            if (result?.status == true) {
                gameId = result.game.id
                if (result.message == "Activity_created") {
                    (requireActivity() as BaseActivity).successToast("Activity Created")
                }
                rootView.summary_back.visibility = View.VISIBLE
                rootView.delete.visibility = View.VISIBLE
                rootView.summary_create_activity.visibility = View.GONE
                rootView.summary_cancel_activity.visibility = View.GONE
                RxView.clicks(rootView.delete).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                    (requireActivity() as BaseActivity).warntoastH("Are you sure you want to delete Activity") {
                        CancelBookings()
                    }
                }
                if (result.message == "Activity_created") {
                    Singleton.isFinish = true
                }
//                view_pager.setCurrentItem(view_pager.currentItem + 1, true)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun hostActivityForInvite() {
        (requireActivity() as BaseActivity).pBar(1)
        if (Singleton.featuresList.isNotEmpty()) {
            Singleton.selectedFacilitiesId.clear()
            Singleton.selectedFacilitiesId.addAll(Singleton.featuresList.map { it.id.toString() })
        }
        if (Singleton.old_id == -1) {
            APIManager.hostActivity(
                (requireActivity() as BaseActivity).returnUserAuthToken(),
                Singleton.selectedSportsName,
                Singleton.selectedSportsId,
                Singleton.selectedVenueName,
                Singleton.selectedFacilitiesId.joinToString { it },
                Singleton.additionalInfo,
                Singleton.skillLevel,
                Singleton.totalPlayers,
                Singleton.confirmedPlayers,
                Singleton.gender,
                Singleton.eventType,
                Singleton.costPerPlay,
                Singleton.pricingType,
                Singleton.selectedTimeSlots.joinToString { it },
                Singleton.selectedDate,
                Singleton.selectedVenueId,
                Singleton.ageMin,
                Singleton.ageMax,
                Singleton.selectedVenueAddress,
                Singleton.selectedPitchId,
                Singleton.gameCost,
                Singleton.userInvites,
                Singleton.groupInvites,
                Singleton.VenueLocation
            ) { result, error ->
                (requireActivity() as BaseActivity).pBar(0)

                if (error != null) {
                    (requireActivity() as BaseActivity).mOnError(error)
                    return@hostActivity
                }

                if (result?.status == true) {
                    gameId = result.game.id
                    if (result.message == "Activity_created") {
                        (requireActivity() as BaseActivity).successToast("Activity Created")
                    }
                    rootView.summary_back.visibility = View.VISIBLE
                    rootView.delete.visibility = View.VISIBLE
                    rootView.summary_create_activity.visibility = View.GONE
                    rootView.summary_cancel_activity.visibility = View.GONE
                    RxView.clicks(rootView.delete).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                        (requireActivity() as BaseActivity).warntoastH("Are you sure you want to delete Activity") {
                            CancelBookings()
                        }
                    }
                    if (result.message == "Activity_created") {
                        Singleton.isFinish = true
                    }
                }
            }
        } else {
            APIManager.hostActivityinvitebookconvert(
                (requireActivity() as BaseActivity).returnUserAuthToken(),
                Singleton.selectedSportsName,
                Singleton.selectedSportsId,
                Singleton.selectedVenueName,
                Singleton.selectedFacilitiesId.joinToString { it },
                Singleton.additionalInfo,
                Singleton.skillLevel,
                Singleton.totalPlayers,
                Singleton.confirmedPlayers,
                Singleton.gender,
                Singleton.eventType,
                Singleton.costPerPlay,
                Singleton.pricingType,
                Singleton.selectedTimeSlots.joinToString { it },
                Singleton.selectedDate,
                Singleton.selectedVenueId,
                Singleton.ageMin,
                Singleton.ageMax,
                Singleton.selectedVenueAddress,
                Singleton.selectedPitchId,
                Singleton.gameCost,
                Singleton.userInvites,
                Singleton.groupInvites,
                Singleton.old_id,
                Singleton.VenueLocation
            ) { result, error ->
                (requireActivity() as BaseActivity).pBar(0)

                if (error != null) {
                    (requireActivity() as BaseActivity).mOnError(error)
                    return@hostActivityinvitebookconvert
                }

                if (result?.status == true) {
                    if (result.message == "Activity_created") {
                        Singleton.isFinish = true
                        (requireActivity() as BaseActivity).successToast("Activity Created")
                    }
                    rootView.summary_back.visibility = View.VISIBLE
                    rootView.delete.visibility = View.VISIBLE
                    rootView.summary_create_activity.visibility = View.GONE
                    rootView.summary_cancel_activity.visibility = View.GONE

                    RxView.clicks(rootView.delete).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                        (requireActivity() as BaseActivity).warntoastH("Are you sure you want to delete Activity") {
                            gameId = Singleton.old_id
                            cancelConversion()
                        }
                    }
//                startActivityFinishAll(this, DashboardActivity::class.java, true, 1)
                    if (result.message == "Event Canceled") {
                        Singleton.isFinish = true
                    }
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun hostActivityForLocationInvite() {
        (requireActivity() as BaseActivity).pBar(1)
        APIManager.hostActivity(
            (requireActivity() as BaseActivity).returnUserAuthToken(),
            Singleton.from,
            Singleton.selectedSportsId,
            Singleton.skillLevel,
            Singleton.totalPlayers,
            Singleton.confirmedPlayers,
            Singleton.gender,
            Singleton.eventType,
            Singleton.costPerPlay,
            Singleton.pricingType,
            Singleton.selectedDate,
            "0",
            Singleton.ageMin,
            Singleton.ageMax,
            Singleton.selectedVenueAddress,
            Singleton.selectedPitch,
            Singleton.gameCost,
            Singleton.selectedTimeSlotStartTime,
            Singleton.selectedTimeSlotEndTime,
            Singleton.pitchCourt,
            Singleton.userInvites,
            Singleton.groupInvites,
            Singleton.selectedFacilitiesId.joinToString { it },
            Singleton.additionalInfo,
            Singleton.latLngFromLocation


        ) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)

            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@hostActivity
            }

            if (result?.status == true) {
                gameId = result.game.id
                if (result.message == "Activity_created") {
                    (requireActivity() as BaseActivity).successToast("Activity Created")
                }
                rootView.summary_back.visibility = View.VISIBLE
                rootView.summary_create_activity.visibility = View.GONE
                rootView.summary_cancel_activity.visibility = View.GONE
                rootView.delete.visibility = View.VISIBLE
                RxView.clicks(rootView.delete).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                    (requireActivity() as BaseActivity).warntoastH("Are you sure you want to delete Activity") {
                        CancelBookings()
                    }
                }
//                view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                if (result.message == "Activity_created") {
                    Singleton.isFinish = true
                }
            }
        }
    }

    private fun CancelBookings() {

        (requireActivity() as BaseActivity).pBar(1)
        APIManager.CancelBookings(
            (requireActivity() as BaseActivity).returnUserAuthToken(),
            gameId.toString()
        ) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)
            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@CancelBookings
            } else if (result!!.status == "true") {
                Singleton.old_id = -1
                (requireActivity() as BaseActivity).successToast(result.message)
                (requireActivity() as BaseActivity).onBackPressed()
            }

        }
    }

    private fun cancelConversion() {

        (requireActivity() as BaseActivity).pBar(1)
        APIManager.CancelBookings(
            (requireActivity() as BaseActivity).returnUserAuthToken(),
            gameId.toString()
        ) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)
            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@CancelBookings
            } else if (result!!.status == "true") {
                (requireActivity() as BaseActivity).successToast(result.message)
                (requireActivity() as BaseActivity).startActivityFinishAll(
                    requireContext(),
                    DashboardActivity::class.java,
                    true,
                    -1,
                )
            }

        }
    }

}