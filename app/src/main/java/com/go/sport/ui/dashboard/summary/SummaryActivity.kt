package com.go.sport.ui.dashboard.summary

/*
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getfeatures.GetFeaturesData
import com.go.sport.model.invitesNew.InvitesFeature
import com.go.sport.model.invites.InvitesInvite
import com.go.sport.model.mygames.Data
import com.go.sport.model.mygames.Feature
import com.go.sport.model.play.PlayData
import com.go.sport.model.play.PlayFeature
import com.go.sport.network.APIManager
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.dashboard.fragments.home.hostactivity.HostActivityActivity
import com.go.sport.ui.dashboard.playerprofile.PlayerProfileActivity
import com.go.sport.ui.dashboard.summary.adapter.*
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_summary.*
import java.util.concurrent.TimeUnit
*/
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.view.View
//import androidx.core.os.bundleOf
//import com.bumptech.glide.Glide
//import com.go.sport.R
//import com.go.sport.base.BaseActivity
//import com.go.sport.model.getfeatures.GetFeaturesData
//import com.go.sport.model.invitesNew.InvitesFeature
//import com.go.sport.model.invites.InvitesInvite
//import com.go.sport.model.mygames.Data
//import com.go.sport.model.mygames.Feature
//import com.go.sport.model.play.PlayData
//import com.go.sport.model.play.PlayFeature
//import com.go.sport.network.APIManager
//import com.go.sport.singleton.Singleton
//import com.go.sport.ui.dashboard.DashboardActivity
//import com.go.sport.ui.dashboard.fragments.home.hostactivity.HostActivityActivity
//import com.go.sport.ui.dashboard.playerprofile.PlayerProfileActivity
//import com.go.sport.ui.dashboard.summary.adapter.*
//import com.jakewharton.rxbinding2.view.RxView
//import kotlinx.android.synthetic.main.activity_summary.*
//import java.util.concurrent.TimeUnit



class SummaryActivity {


    /*private var myGame: Data? = null
    private var invitesInvite: InvitesInvite? = null
    private var playData: PlayData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)


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

    private fun setValues() {
        cont_create_activity.visibility = View.GONE

        if (myGame != null) {

            if (myGame?.type == "booking") {
                cont_create_activity.visibility = View.VISIBLE
                cont_home.visibility = View.VISIBLE
                cont_players.visibility = View.GONE
                tv_price.visibility = View.GONE
                tv_cost_per_player.visibility = View.GONE
                view_players.visibility = View.GONE
            } else {
                cont_create_activity.visibility = View.GONE
            }
            tv_user.text = myGame?.user?.firstName + " " + myGame?.user?.lastName
            if (myGame?.user?.detail != null) {
                Glide.with(this).load(myGame?.user?.detail?.profileImage).into(iv_image)
                tv_gender.text = myGame?.user?.detail?.gender?.capitalize()
                tv_age.text = getAgeFromDOB(
                    myGame?.user?.detail?.dateOfBirth?.split("-")?.get(0)?.toInt() ?: 0,
                    myGame?.user?.detail?.dateOfBirth?.split("-")?.get(1)?.toInt() ?: 0,
                    myGame?.user?.detail?.dateOfBirth?.split("-")?.get(2)?.toInt() ?: 0
                )
            }
            tv_title.text = myGame?.title
           // tv_date.text = myGame?.date
            tv_time.text = myGame?.date
          //zain
             tv_duration.text = "${myGame?.duration}"
            tv_location.text =
                if (!myGame?.facility?.address.isNullOrEmpty()) myGame?.facility?.address else myGame?.detail?.address
            if (myGame?.detail != null) {
                tv_total_players.text = myGame?.detail?.totalPlayers
                tv_confirmed_players.text = myGame?.detail?.confirmedPlayers
                tv_price.text = "AED ${myGame?.detail?.gameFee}"
            }

            if (myGame?.facility != null) {
                tv_pitch.text = myGame?.facility?.pitchsize
                rv_amenities.apply {
                    adapter = VenueFacilitiesAdapter(
                        this@SummaryActivity,
                        myGame?.facility?.feature as ArrayList<GetFeaturesData>
                    )
                }
            } else {
                tv_pitch_heading.visibility = View.GONE
                tv_pitch.visibility = View.GONE
                view_1.visibility = View.GONE
                view_2.visibility = View.GONE
                tv_venue_heading.visibility = View.GONE
                rv_amenities.visibility = View.GONE
            }
            if (myGame?.detail?.eventType == "public")
                getGameJoins(myGame?.id.toString())
            else
                getGameInvites(myGame?.id.toString())
        }

        if (invitesInvite != null) {
            tv_user.text = invitesInvite?.user?.first_name + " " + invitesInvite?.user?.last_name
            if (invitesInvite?.user?.detail != null) {
                Glide.with(this).load(invitesInvite?.user?.detail?.profile_image).into(iv_image)
                tv_gender.text = invitesInvite?.user?.detail?.gender?.capitalize()
                tv_age.text = getAgeFromDOB(
                    invitesInvite?.user?.detail?.date_of_birth?.split("-")?.get(0)?.toInt() ?: 0,
                    invitesInvite?.user?.detail?.date_of_birth?.split("-")?.get(1)?.toInt() ?: 0,
                    invitesInvite?.user?.detail?.date_of_birth?.split("-")?.get(2)?.toInt() ?: 0
                )
            }
            tv_title.text = invitesInvite?.title
         //   tv_date.text = invitesInvite?.date
            tv_time.text = invitesInvite?.date
            tv_duration.text = "${invitesInvite?.duration}"
            tv_location.text = invitesInvite?.facility?.address
            if (invitesInvite?.detail != null) {
                tv_total_players.text = invitesInvite?.detail?.total_players
                tv_confirmed_players.text = invitesInvite?.detail?.confirmed_players
                tv_price.text = "AED ${invitesInvite?.detail?.game_fee}"
            }
            if (invitesInvite?.facility != null) {
                tv_pitch.text = invitesInvite?.facility?.pitchsize
                rv_amenities.apply {
                    adapter = VenueInvitesFacilitiesAdapter(
                        this@SummaryActivity,
                        invitesInvite?.facility?.feature as ArrayList<InvitesFeature>
                    )
                }
            }
            if (invitesInvite?.detail?.event_type == "public")
                getGameJoins(invitesInvite?.id.toString())
            else
                getGameInvites(invitesInvite?.id.toString())
        }

        if (playData != null) {
            tv_user.text = playData?.user?.first_name + " " + playData?.user?.last_name
            if (playData?.user?.detail != null) {
                Glide.with(this).load(playData?.user?.detail?.profile_image).into(iv_image)
                tv_gender.text = playData?.user?.detail?.gender?.capitalize()
                tv_age.text = getAgeFromDOB(
                    playData?.user?.detail?.date_of_birth?.split("-")?.get(0)?.toInt() ?: 0,
                    playData?.user?.detail?.date_of_birth?.split("-")?.get(1)?.toInt() ?: 0,
                    playData?.user?.detail?.date_of_birth?.split("-")?.get(2)?.toInt() ?: 0
                )
            }
            tv_title.text = playData?.title
            //tv_date.text = playData?.date
            tv_time.text = playData?.date
            tv_duration.text = "${playData?.duration}"
            tv_location.text = playData?.facility?.address
            if (playData?.detail != null) {
                tv_total_players.text = playData?.detail?.total_players
                tv_confirmed_players.text = playData?.detail?.confirmed_players
                tv_price.text = "AED ${playData?.detail?.game_fee}"
            }
            if (playData?.facility != null) {
                tv_pitch.text = playData?.facility?.pitchsize
                rv_amenities.apply {
                    adapter = VenuePlayFacilitiesAdapter(
                        this@SummaryActivity,
                        playData?.facility?.feature as ArrayList<PlayFeature>
                    )
                }
            }
            if (playData?.detail?.event_type == "public")
                getGameJoins(playData?.id.toString())
            else
                getGameInvites(playData?.id.toString())


        }
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(cont_home).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivityFinishAll(this, DashboardActivity::class.java, true, -1)
        }

        RxView.clicks(cont_create_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            Singleton.from = "book"
            Singleton.old_id = myGame?.id ?: -1
            Singleton.selectedDate =myGame?.unformatted_date ?: "" //changeFormatTime("dd-MMM-yyyy",myGame?.date ?: "","dd-MM-yyyy" ) //myGame?.date ?: ""
            Singleton.selectedSportsId = myGame?.sport?.id.toString()
            Singleton.selectedVenueAddress = myGame?.facility?.address.toString()
            Singleton.selectedVenueName = myGame?.facilityTitle.toString()
            Singleton.selectedVenueId = myGame?.facilityId.toString()
            Singleton.selectedSportsName = myGame?.sport?.name ?: ""
            Singleton.selectedPitch = myGame?.sport?.name ?: ""
            Singleton.selectedTimeSlotStartTime = myGame?.slot?.joinToString { it.start } ?: ""
            Singleton.selectedTimeSlotEndTime = myGame?.slot?.joinToString { it.end } ?: ""
            Singleton.selectedPitchId = myGame?.detail!!.pitchId
            //zain
            for(item in myGame?.slot!!){
                Singleton.selectedTimeSlots.add(item.id.toString())
            }
           // Singleton.selectedTimeSlots.addAll(myGame?.slot.) = myGame?.slot?.joinToString { it.id.toString() } ?: ""

            Singleton.selectedFacilitiesId.clear()
            Singleton.selectedFacilitiesId.add(myGame?.facilityId ?: "")
            Singleton.selectedFacilities.clear()
            Singleton.selectedFacilities.addAll(myGame?.features ?: ArrayList())
            Singleton.gameCost = myGame?.detail?.gameFee?:"0"
            startActivity(this, HostActivityActivity::class.java, true, 1)
        }
    }

    private fun getGameJoins(id: String) {
        pBar(1)
        APIManager.gameJoins(returnUserAuthToken(), id) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@gameJoins
            }

            if (result != null) {
                rv_players.adapter = SummaryPlayersAdapter(
                    this,
                    result.data,
                    object : SummaryPlayersAdapter.SummaryPlayersClickListener {
                        override fun onSummaryPlayersItemClick(position: Int) {
                            startActivity(
                                this@SummaryActivity,
                                PlayerProfileActivity::class.java,
                                false,
                                1,
                                bundleOf(Pair("user_id", result.data[position].userId))
                            )
                        }
                    })
            }
        }
    }

    private fun getGameInvites(id: String) {
        pBar(1)
        APIManager.gameInvites(returnUserAuthToken(), id) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@gameInvites
            }

            if (result != null) {
                rv_players.adapter = SummaryInvitesAdapter(
                    this,
                    result.data,
                    object : SummaryInvitesAdapter.SummaryInvitesClickListener {
                        override fun onSummaryInvitesItemClick(position: Int) {
                            startActivity(
                                this@SummaryActivity,
                                PlayerProfileActivity::class.java,
                                false,
                                1,
                                bundleOf(Pair("user_id", result.data[position].userId))
                            )
                        }
                    })
            }
        }
    }

    private fun bookFacility() {

        pBar(1)

        APIManager.bookFacility(
            returnUserAuthToken(),
            myGame?.sport?.name ?: "",
            myGame?.sportsId ?: "",
            myGame?.date ?: "",
            myGame?.facilityId.toString(),
            myGame?.detail?.pitchId ?: "",
            myGame?.facility?.pricing ?: "",
            myGame?.completeTime ?: "",
            myGame?.detail?.address ?: ""
        ) { result, error ->

            pBar(0)

            if (error != null) {
                mOnError(error)
                return@bookFacility
            }

            if (result?.status == true) {
                successToast(result.message)
                startActivityFinishAll(this, DashboardActivity::class.java, true, 1)
            }


        }
    }*/
//    private var myGame: Data? = null
//    private var invitesInvite: InvitesInvite? = null
//    private var playData: PlayData? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_summary)
//
//
//        myGame = Singleton.homeClickedMyGamesModel
//        invitesInvite = Singleton.invitesInvite
//        playData = Singleton.playData
//        initListeners()
//        setValues()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Singleton.homeClickedMyGamesModel = null
//        Singleton.invitesInvite = null
//        Singleton.playData = null
//    }
//
//    private fun setValues() {
//        cont_create_activity.visibility = View.GONE
//
//        if (myGame != null) {
//
//            if (myGame?.type == "booking") {
//                cont_create_activity.visibility = View.VISIBLE
//                cont_home.visibility = View.VISIBLE
//                cont_players.visibility = View.GONE
//                tv_price.visibility = View.GONE
//                tv_cost_per_player.visibility = View.GONE
//                view_players.visibility = View.GONE
//            } else {
//                cont_create_activity.visibility = View.GONE
//            }
//            tv_user.text = myGame?.user?.firstName + " " + myGame?.user?.lastName
//            if (myGame?.user?.detail != null) {
//                Glide.with(this).load(myGame?.user?.detail?.profileImage).into(iv_image)
//                tv_gender.text = myGame?.user?.detail?.gender?.capitalize()
//                tv_age.text = getAgeFromDOB(
//                    myGame?.user?.detail?.dateOfBirth?.split("-")?.get(0)?.toInt() ?: 0,
//                    myGame?.user?.detail?.dateOfBirth?.split("-")?.get(1)?.toInt() ?: 0,
//                    myGame?.user?.detail?.dateOfBirth?.split("-")?.get(2)?.toInt() ?: 0
//                )
//            }
//            tv_title.text = myGame?.title
//           // tv_date.text = myGame?.date
//            tv_time.text = myGame?.date
//          //zain
//             tv_duration.text = "${myGame?.duration}"
//            tv_location.text =
//                if (!myGame?.facility?.address.isNullOrEmpty()) myGame?.facility?.address else myGame?.detail?.address
//            if (myGame?.detail != null) {
//                tv_total_players.text = myGame?.detail?.totalPlayers
//                tv_confirmed_players.text = myGame?.detail?.confirmedPlayers
//                tv_price.text = "AED ${myGame?.detail?.gameFee}"
//            }
//
//            if (myGame?.facility != null) {
//                tv_pitch.text = myGame?.facility?.pitchsize
//                rv_amenities.apply {
//                    adapter = VenueFacilitiesAdapter(
//                        this@SummaryActivity,
//                        myGame?.facility?.feature as ArrayList<GetFeaturesData>
//                    )
//                }
//            } else {
//                tv_pitch_heading.visibility = View.GONE
//                tv_pitch.visibility = View.GONE
//                view_1.visibility = View.GONE
//                view_2.visibility = View.GONE
//                tv_venue_heading.visibility = View.GONE
//                rv_amenities.visibility = View.GONE
//            }
//            if (myGame?.detail?.eventType == "public")
//                getGameJoins(myGame?.id.toString())
//            else
//                getGameInvites(myGame?.id.toString())
//        }
//
//        if (invitesInvite != null) {
//            tv_user.text = invitesInvite?.user?.first_name + " " + invitesInvite?.user?.last_name
//            if (invitesInvite?.user?.detail != null) {
//                Glide.with(this).load(invitesInvite?.user?.detail?.profile_image).into(iv_image)
//                tv_gender.text = invitesInvite?.user?.detail?.gender?.capitalize()
//                tv_age.text = getAgeFromDOB(
//                    invitesInvite?.user?.detail?.date_of_birth?.split("-")?.get(0)?.toInt() ?: 0,
//                    invitesInvite?.user?.detail?.date_of_birth?.split("-")?.get(1)?.toInt() ?: 0,
//                    invitesInvite?.user?.detail?.date_of_birth?.split("-")?.get(2)?.toInt() ?: 0
//                )
//            }
//            tv_title.text = invitesInvite?.title
//         //   tv_date.text = invitesInvite?.date
//            tv_time.text = invitesInvite?.date
//            tv_duration.text = "${invitesInvite?.duration}"
//            tv_location.text = invitesInvite?.facility?.address
//            if (invitesInvite?.detail != null) {
//                tv_total_players.text = invitesInvite?.detail?.total_players
//                tv_confirmed_players.text = invitesInvite?.detail?.confirmed_players
//                tv_price.text = "AED ${invitesInvite?.detail?.game_fee}"
//            }
//            if (invitesInvite?.facility != null) {
//                tv_pitch.text = invitesInvite?.facility?.pitchsize
//                rv_amenities.apply {
//                    adapter = VenueInvitesFacilitiesAdapter(
//                        this@SummaryActivity,
//                        invitesInvite?.facility?.feature as ArrayList<InvitesFeature>
//                    )
//                }
//            }
//            if (invitesInvite?.detail?.event_type == "public")
//                getGameJoins(invitesInvite?.id.toString())
//            else
//                getGameInvites(invitesInvite?.id.toString())
//        }
//
//        if (playData != null) {
//            tv_user.text = playData?.user?.first_name + " " + playData?.user?.last_name
//            if (playData?.user?.detail != null) {
//                Glide.with(this).load(playData?.user?.detail?.profile_image).into(iv_image)
//                tv_gender.text = playData?.user?.detail?.gender?.capitalize()
//                tv_age.text = getAgeFromDOB(
//                    playData?.user?.detail?.date_of_birth?.split("-")?.get(0)?.toInt() ?: 0,
//                    playData?.user?.detail?.date_of_birth?.split("-")?.get(1)?.toInt() ?: 0,
//                    playData?.user?.detail?.date_of_birth?.split("-")?.get(2)?.toInt() ?: 0
//                )
//            }
//            tv_title.text = playData?.title
//            //tv_date.text = playData?.date
//            tv_time.text = playData?.date
//            tv_duration.text = "${playData?.duration}"
//            tv_location.text = playData?.facility?.address
//            if (playData?.detail != null) {
//                tv_total_players.text = playData?.detail?.total_players
//                tv_confirmed_players.text = playData?.detail?.confirmed_players
//                tv_price.text = "AED ${playData?.detail?.game_fee}"
//            }
//            if (playData?.facility != null) {
//                tv_pitch.text = playData?.facility?.pitchsize
//                rv_amenities.apply {
//                    adapter = VenuePlayFacilitiesAdapter(
//                        this@SummaryActivity,
//                        playData?.facility?.feature as ArrayList<PlayFeature>
//                    )
//                }
//            }
//            if (playData?.detail?.event_type == "public")
//                getGameJoins(playData?.id.toString())
//            else
//                getGameInvites(playData?.id.toString())
//
//
//        }
//    }
//
//    @SuppressLint("CheckResult")
//    private fun initListeners() {
//        RxView.clicks(iv_back).subscribe {
//            super.onBackPressed()
//        }
//
//        RxView.clicks(cont_home).throttleFirst(2, TimeUnit.SECONDS).subscribe {
//            startActivityFinishAll(this, DashboardActivity::class.java, true, -1)
//        }
//
//        RxView.clicks(cont_create_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
//            Singleton.from = "book"
//            Singleton.old_id = myGame?.id ?: -1
//            Singleton.selectedDate =myGame?.unformatted_date ?: "" //changeFormatTime("dd-MMM-yyyy",myGame?.date ?: "","dd-MM-yyyy" ) //myGame?.date ?: ""
//            Singleton.selectedSportsId = myGame?.sport?.id.toString()
//            Singleton.selectedVenueAddress = myGame?.facility?.address.toString()
//            Singleton.selectedVenueName = myGame?.facilityTitle.toString()
//            Singleton.selectedVenueId = myGame?.facilityId.toString()
//            Singleton.selectedSportsName = myGame?.sport?.name ?: ""
//            Singleton.selectedPitch = myGame?.sport?.name ?: ""
//            Singleton.selectedTimeSlotStartTime = myGame?.slot?.joinToString { it.start } ?: ""
//            Singleton.selectedTimeSlotEndTime = myGame?.slot?.joinToString { it.end } ?: ""
//            Singleton.selectedPitchId = myGame?.detail!!.pitchId
//            //zain
//            for(item in myGame?.slot!!){
//                Singleton.selectedTimeSlots.add(item.id.toString())
//            }
//           // Singleton.selectedTimeSlots.addAll(myGame?.slot.) = myGame?.slot?.joinToString { it.id.toString() } ?: ""
//
//            Singleton.selectedFacilitiesId.clear()
//            Singleton.selectedFacilitiesId.add(myGame?.facilityId ?: "")
//            Singleton.selectedFacilities.clear()
//            Singleton.selectedFacilities.addAll(myGame?.features ?: ArrayList())
//            Singleton.gameCost = myGame?.detail?.gameFee?:"0"
//            startActivity(this, HostActivityActivity::class.java, true, 1)
//        }
//    }
//
//    private fun getGameJoins(id: String) {
//        pBar(1)
//        APIManager.gameJoins(returnUserAuthToken(), id) { result, error ->
//            pBar(0)
//
//            if (error != null) {
//                mOnError(error)
//                return@gameJoins
//            }
//
//            if (result != null) {
//                rv_players.adapter = SummaryPlayersAdapter(
//                    this,
//                    result.data,
//                    object : SummaryPlayersAdapter.SummaryPlayersClickListener {
//                        override fun onSummaryPlayersItemClick(position: Int) {
//                            startActivity(
//                                this@SummaryActivity,
//                                PlayerProfileActivity::class.java,
//                                false,
//                                1,
//                                bundleOf(Pair("user_id", result.data[position].userId))
//                            )
//                        }
//                    })
//            }
//        }
//    }
//
//    private fun getGameInvites(id: String) {
//        pBar(1)
//        APIManager.gameInvites(returnUserAuthToken(), id) { result, error ->
//            pBar(0)
//
//            if (error != null) {
//                mOnError(error)
//                return@gameInvites
//            }
//
//            if (result != null) {
//                rv_players.adapter = SummaryInvitesAdapter(
//                    this,
//                    result.data,
//                    object : SummaryInvitesAdapter.SummaryInvitesClickListener {
//                        override fun onSummaryInvitesItemClick(position: Int) {
//                            startActivity(
//                                this@SummaryActivity,
//                                PlayerProfileActivity::class.java,
//                                false,
//                                1,
//                                bundleOf(Pair("user_id", result.data[position].userId))
//                            )
//                        }
//                    })
//            }
//        }
//    }
//
//    private fun bookFacility() {
//
//        pBar(1)
//
//        APIManager.bookFacility(
//            returnUserAuthToken(),
//            myGame?.sport?.name ?: "",
//            myGame?.sportsId ?: "",
//            myGame?.date ?: "",
//            myGame?.facilityId.toString(),
//            myGame?.detail?.pitchId ?: "",
//            myGame?.facility?.pricing ?: "",
//            myGame?.completeTime ?: "",
//            myGame?.detail?.address ?: "",
////            myGame?.detail?.boo
//        ) { result, error ->
//
//            pBar(0)
//
//            if (error != null) {
//                mOnError(error)
//                return@bookFacility
//            }
//
//            if (result?.status == true) {
//                successToast(result.message)
//                startActivityFinishAll(this, DashboardActivity::class.java, true, 1)
//            }
//
//
//        }
//    }
}