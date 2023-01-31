package com.go.sport.newui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.extensions.setGlide
import com.go.sport.model.firebase.ChatHeadsModel
import com.go.sport.model.gamejoins.GameJoinsData
import com.go.sport.model.getfeatures.GetFeaturesData
import com.go.sport.model.invites.InvitesInvite
import com.go.sport.model.mygames.Data
import com.go.sport.model.play.PlayData
import com.go.sport.network.APIManager
import com.go.sport.newui.adapter.NewSummaryJoinPlayersAdapter
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.chats.ConversationActivity
import com.go.sport.ui.dashboard.playerprofile.PlayerProfileActivity
import com.go.sport.ui.dashboard.summary.adapter.*
import com.go.sport.ui.dashboard.venueAdapter.AmenetiesAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_new_summary_screen.*
import kotlinx.android.synthetic.main.activity_new_summary_screen.ic_back
import kotlinx.android.synthetic.main.activity_new_summary_screen.tv_age
import kotlinx.android.synthetic.main.activity_new_summary_screen.tv_gender
import kotlinx.android.synthetic.main.activity_new_summary_screen.tv_pitch
import kotlinx.android.synthetic.main.activity_new_summary_screen.tv_title
import kotlinx.android.synthetic.main.activity_new_summary_screen.tv_user

import kotlinx.android.synthetic.main.bottom_sheet_summary_view_all.*
import kotlinx.android.synthetic.main.row_invites.*

import java.util.concurrent.TimeUnit

class NewSummaryScreen : BaseActivity() {
    private var joinedPlayer = ArrayList<GameJoinsData>()
    private var myGame: Data? = null
    private var invitesInvite: InvitesInvite? = null
    private var playData: PlayData? = null
    private var facilities = arrayListOf<GetFeaturesData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_summary_screen)

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
        Singleton.SummaryFrom = false
    }

    @SuppressLint("CheckResult")
    private fun setValues() {

        if (Singleton.isPastGame) {
            back_activity.visibility = View.VISIBLE
            cont_cancel_activity.visibility = View.GONE
        }

        if (myGame != null) {
            myGamesData()
        }

        if (invitesInvite != null) {
            invitesData()
        }

        if (playData != null) {
            playData()
        }
    }

    private fun myGamesData() {
        if (myGame?.user?.id == MySharedPreference(this).getUserObject()?.id) {
            cont_cancel_activity.visibility = View.VISIBLE
        } else {
            cont_cancel_activity.visibility = View.GONE
        }
        if (Singleton.SummaryFrom) {
            cont_join_activity.visibility = View.GONE
            cont_leave_activity.visibility = View.GONE
        }

        RxView.clicks(cont_leave_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (myGame?.user?.id != MySharedPreference(this).getUserObject()?.id) {
                warntoastH("Are you sure you want to leave game?") { leaveMyGame(true) }
            } else {
                cont_leave_activity.visibility = View.GONE
            }
        }

        tv_user.text =
            myGame?.user?.firstName?.capitalize() + " " + myGame?.user?.lastName?.capitalize()
        if (myGame?.user?.detail != null) {
            if (myGame?.user?.detail?.profileImage == "") {
                Glide.with(this).load(R.drawable.avatar).into(ic_image)
            } else {
                Glide.with(this).load(myGame?.user?.detail?.profileImage).into(ic_image)
            }
            tv_gender.text = myGame?.user?.detail?.gender?.capitalize()
            tv_age.text = getAgeFromDOB(
                myGame?.user?.detail?.dateOfBirth?.split("-")?.get(0)?.toInt() ?: 0,
                myGame?.user?.detail?.dateOfBirth?.split("-")?.get(1)?.toInt() ?: 0,
                myGame?.user?.detail?.dateOfBirth?.split("-")?.get(2)?.toInt() ?: 0
            ) + " Years"
        }
        if (myGame?.type == "out_of_app_activity") {
            if (myGame?.user?.id != MySharedPreference(this).getUserObject()?.id) {
                cont_cancel_activity.visibility = View.GONE
                cont_leave_activity.visibility = View.VISIBLE
            }
            tv_title.text = myGame?.detail?.address?.capitalize()
            tv_pitch.text = myGame?.detail?.pitchCourt?.capitalize()
            time_zone.text =
                myGame?.detail?.startTimingOut + " - " + myGame?.detail?.endTimingOut
        } else {
            tv_title.text = myGame?.facility?.name?.capitalize()
            tv_pitch.text = myGame?.facility?.pitchsize
            time_zone.text = myGame?.completeTime
            cont_cancel_activity.visibility = View.VISIBLE
            cont_leave_activity.visibility = View.GONE
        }

        date_view.text = myGame?.date
        age_view.text = myGame?.detail?.ageMin + " - " + myGame?.detail?.ageMax
        skill_set.text = myGame?.detail?.skills?.capitalize()
        total_players_view.text = myGame?.detail?.totalPlayers
        confrm_players.text = myGame?.detail?.confirmedPlayers
        player_cost.text = myGame?.currency?.capitalize() + " " + myGame?.detail?.costPerPlay
        payment_type.text = myGame?.detail?.paymentType?.capitalize()
        iv_sports.setGlide(myGame?.sport?.image ?: "")
        tv_sports_title.text = myGame?.sport?.name?.capitalize()
        if (myGame?.detail?.additionalInformation != "") {
            additional_info.visibility = View.VISIBLE
            extra.visibility = View.VISIBLE
            tv_add_details.text = myGame?.detail?.additionalInformation?.capitalize()
        } else {
            additional_info.visibility = View.GONE
            extra.visibility = View.GONE
        }



        if (myGame?.facility != null) {
            rv_amenities.apply {
                adapter = AmenetiesAdapter(
                    this@NewSummaryScreen,
                    myGame?.facility?.feature as ArrayList<GetFeaturesData>
                )
            }
        } else {
            var facitiyes = myGame?.detail?.facilityFeatures?.split(",")
            if (facitiyes != null) {
                for (faciltyid in facitiyes) {
                    for (amenity in Singleton.features) {
                        if (faciltyid.trim() == amenity.id.trim()) {
                            facilities.add(amenity)

                            break;
                        }
                    }
                }
            }
            rv_amenities.apply {
                adapter = AmenetiesAdapter(
                    this@NewSummaryScreen,
                    facilities
                )
            }
        }
        //            if (invitesInvite?.detail?.event_type == "public")
        getGameJoins(myGame?.id.toString(), true)
    }

    private fun invitesData() {
        if (invitesInvite?.user?.detail?.id == MySharedPreference(this).getUserObject()?.id) {
            cont_cancel_activity.visibility = View.VISIBLE
        } else {
            cont_cancel_activity.visibility = View.GONE
        }

        RxView.clicks(cont_leave_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            warntoastH("Are you sure you want to leave game?") { leaveGame(true) }
        }

        RxView.clicks(cont_join_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (invitesInvite?.detail?.payment_type == "wallet") {
                disclaimer(
                    "Terms & Conditions\n\n" +
                            "In case of wallet payment, the users will be asked to pay the amount in order to confirm their participation. \n\n" +
                            "Payment collected from users will be credited to your GoPlay wallet upon completion of activity. These can be used for transactions that you make on GoPlay and cannot be redeemed by way of cash or bank transfers.\n\n" +
                            "In case there is a dispute with respect to payment or activity, GoPlay will investigate the matter and in doing so, reserves the right to take corrective measures to amicably resolve the dispute.\n\n"
                ) { respondToInvite(true) }
            } else if (invitesInvite?.detail?.payment_type == "both") {
                paymentToast("Please select one payment type to pay", {
                    disclaimer(
                        "Terms & Conditions\n\n" +
                                "In case of wallet payment, the users will be asked to pay the amount in order to confirm their participation. \n\n" +
                                "Payment collected from users will be credited to your GoPlay wallet upon completion of activity. These can be used for transactions that you make on GoPlay and cannot be redeemed by way of cash or bank transfers.\n\n" +
                                "In case there is a dispute with respect to payment or activity, GoPlay will investigate the matter and in doing so, reserves the right to take corrective measures to amicably resolve the dispute.\n\n"
                    ) { respondToInvite(true) }
                }, {
                    respondToInvite(true)
                })
            } else {
                respondToInvite(true)
            }
        }
        tv_user.text =
            invitesInvite?.user?.first_name?.capitalize() + " " + invitesInvite?.user?.last_name?.capitalize()
        if (invitesInvite?.user?.detail != null) {
            if (invitesInvite?.user?.detail?.profile_image == "") {
                Glide.with(this).load(R.drawable.avatar).into(ic_image)
            } else {
                Glide.with(this).load(invitesInvite?.user?.detail?.profile_image)
                    .into(ic_image)
            }
            tv_gender.text = invitesInvite?.user?.detail?.gender?.capitalize()
            tv_age.text = getAgeFromDOB(
                invitesInvite?.user?.detail?.date_of_birth?.split("-")?.get(0)?.toInt()
                    ?: 0,
                invitesInvite?.user?.detail?.date_of_birth?.split("-")?.get(1)?.toInt()
                    ?: 0,
                invitesInvite?.user?.detail?.date_of_birth?.split("-")?.get(2)?.toInt() ?: 0
            ) + " Years"
            tv_title.text = invitesInvite?.detail?.address?.capitalize()
        }
        //   tv_date.text = invitesInvite?.date
        time_zone.text = invitesInvite?.complete_time
        if (invitesInvite?.detail?.pitch_court != "N/A") {
            tv_pitch.text = invitesInvite?.detail?.pitch_court
        } else {
            tv_pitch.text = invitesInvite?.sport?.name
        }
        date_view.text = invitesInvite?.date


        if (invitesInvite?.detail?.additional_information != "") {
            additional_info.visibility = View.VISIBLE
            extra.visibility = View.VISIBLE
            tv_add_details.text = invitesInvite?.facility?.description?.capitalize()
        } else {
            additional_info.visibility = View.GONE
            extra.visibility = View.GONE
        }
        if (invitesInvite?.detail != null) {
            age_view.text =
                invitesInvite?.detail?.age_min + " - " + invitesInvite?.detail?.age_max
            skill_set.text = invitesInvite?.detail?.skills?.capitalize()
            total_players_view.text = invitesInvite?.detail?.total_players
            confrm_players.text = invitesInvite?.detail?.confirmed_players
            payment_type.text = invitesInvite?.detail?.payment_type?.capitalize()
            player_cost.text = "PKR ${invitesInvite?.detail?.cost_per_play}"
        }

        if (invitesInvite?.facility != null) {
//                tv_pitch.text = invitesInvite?.facility?.pitchsize
            rv_amenities.apply {
                adapter = AmenetiesAdapter(
                    this@NewSummaryScreen,
                    facilities
                )
            }
        } else {
            var facitiyes = invitesInvite?.detail?.facility_features?.split(",")
            if (facitiyes != null) {
                for (faciltyid in facitiyes) {
                    for (amenity in Singleton.features) {
                        if (faciltyid.trim() == amenity.id.trim()) {
                            facilities.add(amenity)

                            break;
                        }
                    }
                }
            }
            rv_amenities.apply {
                adapter = AmenetiesAdapter(
                    this@NewSummaryScreen,
                    facilities
                )
            }
        }
//            if (invitesInvite?.detail?.event_type == "public")
        getGameJoins(invitesInvite?.id.toString(), true)
    }

    private fun playData() {
        if (playData?.user?.id == MySharedPreference(this).getUserObject()?.id) {
            cont_cancel_activity.visibility = View.VISIBLE
        } else {
            cont_cancel_activity.visibility = View.GONE
        }
        RxView.clicks(cont_join_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {

            if (playData?.detail?.total_players != playData?.detail?.confirmed_players) {
                if (playData?.detail?.payment_type == "wallet") {
                    disclaimer(
                        "Terms & Conditions\n\n" +
                                "In case of wallet payment, the users will be asked to pay the amount in order to confirm their participation. \n\n" +
                                "Payment collected from users will be credited to your GoPlay wallet upon completion of activity. These can be used for transactions that you make on GoPlay and cannot be redeemed by way of cash or bank transfers.\n\n" +
                                "In case there is a dispute with respect to payment or activity, GoPlay will investigate the matter and in doing so, reserves the right to take corrective measures to amicably resolve the dispute.\n\n"
                    ) {
                        joinAGame(true)
                    }
                } else if (playData?.detail?.payment_type == "both") {

                    paymentToast("Please select one payment type to pay", {
                        disclaimer(
                            "Terms & Conditions\n\n" +
                                    "In case of wallet payment, the users will be asked to pay the amount in order to confirm their participation. \n\n" +
                                    "Payment collected from users will be credited to your GoPlay wallet upon completion of activity. These can be used for transactions that you make on GoPlay and cannot be redeemed by way of cash or bank transfers.\n\n" +
                                    "In case there is a dispute with respect to payment or activity, GoPlay will investigate the matter and in doing so, reserves the right to take corrective measures to amicably resolve the dispute.\n\n"
                        ) {
                            joinAGame(true)
                        }
                    }, {
                        joinAGame(true)
                    })
                } else {
                    joinAGame(true)
                }
            } else {
                infoToast("Player's join count is full")
            }

        }
        RxView.clicks(cont_leave_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            warntoastH("Are you sure you want to leave game?") { leavePlayGame(true) }

        }
        tv_user.text =
            playData?.user?.first_name?.capitalize() + " " + playData?.user?.last_name?.capitalize()
        if (playData?.user?.detail != null) {
            if (playData?.user?.detail?.profile_image == "") {
                Glide.with(this).load(R.drawable.avatar).into(ic_image)
            } else {
                Glide.with(this).load(playData?.user?.detail?.profile_image).into(ic_image)
            }
            tv_gender.text = playData?.user?.detail?.gender?.capitalize()
            tv_age.text = getAgeFromDOB(
                playData?.user?.detail?.date_of_birth?.split("-")?.get(0)?.toInt() ?: 0,
                playData?.user?.detail?.date_of_birth?.split("-")?.get(1)?.toInt() ?: 0,
                playData?.user?.detail?.date_of_birth?.split("-")?.get(2)?.toInt() ?: 0
            ) + " Years"
        }
        age_view.text = playData?.detail?.age_min + " - " + playData?.detail?.age_max
        skill_set.text = playData?.detail?.skills?.capitalize()
        tv_title.text = playData?.detail?.address?.capitalize()
        date_view.text = playData?.date
        if (playData?.detail?.start_timing_out == "N/A" && playData?.detail?.end_timing_out == "N/A") {
            time_zone.text = playData?.complete_time
        } else {
            time_zone.text =
                playData?.detail?.start_timing_out + " - " + playData?.detail?.end_timing_out
        }
        if (playData?.detail?.pitch_court == "N/A") {
            tv_pitch.text = playData?.facility?.pitchsize?.capitalize()
        } else {
            tv_pitch.text = playData?.detail?.pitch_court?.capitalize()
        }
        if (playData?.facility?.description != null) {
            additional_info.visibility = View.VISIBLE
            extra.visibility = View.VISIBLE
            tv_add_details.text = playData?.facility?.description?.capitalize()
        } else if (playData?.detail?.additional_information != "") {
            additional_info.visibility = View.VISIBLE
            extra.visibility = View.VISIBLE
            tv_add_details.text = playData?.detail?.additional_information?.capitalize()
        } else {
            additional_info.visibility = View.GONE
            extra.visibility = View.GONE
        }
        if (playData?.detail != null) {
            total_players_view.text = playData?.detail?.total_players
            confrm_players.text = playData?.detail?.confirmed_players
            payment_type.text = playData?.detail?.payment_type?.capitalize()
            player_cost.text = playData?.currency + " " + playData?.detail?.cost_per_play
        }


        if (playData?.facility != null) {
//                tv_pitch.text = invitesInvite?.facility?.pitchsize
            rv_amenities.apply {
                adapter = AmenetiesAdapter(
                    this@NewSummaryScreen,
                    facilities
                )
            }
        } else {
            var facitiyes = playData?.detail?.facility_features?.split(",")
            if (facitiyes != null) {
                for (faciltyid in facitiyes) {
                    for (amenity in Singleton.features) {
                        if (faciltyid.trim() == amenity.id.trim()) {
                            facilities.add(amenity)

                            break;
                        }
                    }
                }
            }
            rv_amenities.apply {
                adapter = AmenetiesAdapter(
                    this@NewSummaryScreen,
                    facilities
                )
            }
        }

        getGameJoins(playData?.id.toString(), true)
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(tv_view_all).subscribe {
            bottomSheet()
        }
        RxView.clicks(ic_back).subscribe {
            Singleton.isPastGame = false
            onBackPressed()
        }
        RxView.clicks(back_activity).subscribe {
            onBackPressed()
        }
        RxView.clicks(cont_cancel_activity).subscribe {
            if (joinedPlayer.size != 0) {
                if (myGame?.detail?.paymentType == "wallet" || myGame?.detail?.paymentType == "both") {
                    infoToast("You can't cancel this activity due to wallet selection")
                } else {
                    warntoastH("Are you sure you want to delete Activity?") {
                        cancelActivity(true)
                    }
                }
            } else {
                warntoastH("Are you sure you want to delete Activity?") {
                    cancelActivity(true)
                }
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
                                this@NewSummaryScreen,
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

        val c = myGame?.lat_lng?.split(",")?.get(0)?.trim()
        val d = myGame?.lat_lng?.split(",")?.get(1)?.trim()
        RxView.clicks(location).throttleFirst(2, TimeUnit.SECONDS).subscribe {

            if (myGame != null) {
                if (myGame?.type == "out_of_app_activity" || myGame?.type == "activity") {
                    val gmmIntentUri = Uri.parse("google.navigation:q=$c,$d")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)
                }
            } else {
                val gmmIntentUri = Uri.parse("google.navigation:q=$a,$b")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }

    private fun cancelActivity(isRefresh: Boolean = false) {
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

    private fun bottomSheet() {
        val bsd = BottomSheetDialog(this)
        bsd.apply {
            setContentView(R.layout.bottom_sheet_summary_view_all)

            rv_joined_players.adapter = NewSummaryJoinPlayersAdapter(
                this@NewSummaryScreen,
                joinedPlayer,
                object : NewSummaryJoinPlayersAdapter.SummaryPlayersClickListener {
                    override fun onSummaryJoinedPlayersItemClick(position: Int) {
                        startActivity(
                            this@NewSummaryScreen,
                            PlayerProfileActivity::class.java,
                            false,
                            1,
                            bundleOf(Pair("user_id", joinedPlayer[position].userId))
                        )
                    }
                }
            )
            bsd.show()
        }
    }

    var iAmOrganizer = false;

    private fun getGameJoins(id: String, isRefresh: Boolean = false) {
        pBar(1)
        APIManager.gameJoins(returnUserAuthToken(), id) { result, error ->
            pBar(0)
            if (isRefresh)

                if (error != null) {
                    mOnError(error)
                    return@gameJoins
                }

            if (result != null) {

                joinedPlayer.addAll(result.data)
                if (joinedPlayer.size != 0 && myGame?.user?.id != MySharedPreference(this).getUserObject()?.id) {
                    tv_view_all.visibility = View.VISIBLE
                    rv_players.visibility = View.VISIBLE
                    players_list.visibility = View.GONE
                    for (user in joinedPlayer) {
                        if (user.userId.toInt() == MySharedPreference(this).getUserObject()?.id) {
                            cont_join_activity.visibility = View.GONE
                            cont_leave_activity.visibility = View.VISIBLE
                        } else {
                            cont_join_activity.visibility = View.VISIBLE
                            cont_leave_activity.visibility = View.GONE
                        }
                    }

                } else if (joinedPlayer.size == 0 && myGame?.userId?.toInt() != MySharedPreference(
                        this
                    ).getUserObject()?.id
                ) {
                    players_list.visibility = View.VISIBLE
                    tv_view_all.visibility = View.GONE
                    rv_players.visibility = View.GONE
                    cont_join_activity.visibility = View.VISIBLE
                } else {
                    players_list.visibility = View.VISIBLE
                    tv_view_all.visibility = View.GONE
                    rv_players.visibility = View.GONE
                    cont_join_activity.visibility = View.GONE
                }
                //if (joinedPlayer.user.id == MySharedPreference(this).getUserObject()?.id)

                rv_players.adapter = SummaryPlayersAdapter(
                    this,
                    result.data,
                    object : SummaryPlayersAdapter.SummaryPlayersClickListener {
                        override fun onSummaryPlayersItemClick(position: Int) {
                            startActivity(
                                this@NewSummaryScreen,
                                PlayerProfileActivity::class.java,
                                false,
                                1,
                                bundleOf(Pair("user_id", result.data[position].userId))
                            )
                        }
                    })
                if (iAmOrganizer) {
                    cont_join_activity.visibility = View.GONE
                    cont_leave_activity.visibility = View.GONE
                }

            }
        }
    }

    private fun leavePlayGame(isRefresh: Boolean = false) {
        pBar(1)
        playData?.detail?.game_id?.let {
            APIManager.leaveGamePlay(
                returnUserAuthToken(), it
            ) { result, error ->
                pBar(0)
                if (isRefresh)

                    if (error != null) {
                        mOnError(error)
                        return@leaveGamePlay
                    }
                if (result?.status == true) {
                    getGameJoins(playData?.detail?.game_id.toString())
                    if (result.message == "Left game") {
                        isRefresh
                        apiToast("Game Left Successfully") {
                            cont_leave_activity.visibility = View.GONE
                            cont_join_activity.visibility = View.VISIBLE
                        }

                    }
                }
            }
        }
    }

    private fun leaveGame(isRefresh: Boolean = false) {
        pBar(1)
        APIManager.leaveGame(
            returnUserAuthToken(),
            invitesInvite?.invite?.id.toString(),
            invitesInvite?.invite?.game_id.toString()
        ) { result, error ->
            pBar(0)

            if (isRefresh)
                if (error != null) {
                    mOnError(error)
                    return@leaveGame
                }
            if (result?.status == true) {
                if (result.message == "Left game") {
                    isRefresh
                    successToast("Game Left Successfully")
                    cont_join_activity.visibility = View.GONE
                    cont_leave_activity.visibility = View.VISIBLE
                }
            }
        }
    }

    fun joinAGame(isRefresh: Boolean = false) {
        pBar(1)
        playData?.detail?.game_id?.let {
            APIManager.joinGame(
                returnUserAuthToken(),
                it
            ) { result, error ->
                pBar(0)

                if (isRefresh)
                    if (error != null) {
                        mOnError(error)
                        return@joinGame
                    }

                if (result?.status == true) {
                    if (result.message == "Joined.") {
                        isRefresh
                        successToast("Game Joined Successfully")
                    }
                    cont_join_activity.visibility = View.GONE
                    cont_leave_activity.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun leaveMyGame(isRefresh: Boolean = false) {
        pBar(1)
        myGame?.detail?.gameId.let {
            if (it != null) {
                APIManager.leaveGamePlay(
                    returnUserAuthToken(), it
                ) { result, error ->
                    pBar(0)

                    if (isRefresh)
                        if (error != null) {
                            mOnError(error)
                            return@leaveGamePlay
                        }
                    if (result?.status == true) {
                        if (result.message == "Left game") {
                            isRefresh
                            successToast("Game Left Successfully")
                            getGameJoins(myGame?.id.toString())
                            cont_leave_activity.visibility = View.GONE
                            cont_join_activity.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun respondToInvite(isRefresh: Boolean = false) {
        pBar(1)

        invitesInvite?.invite?.id.toString().let {
            invitesInvite?.invite?.game_id?.let { it1 ->
                APIManager.respondInvite(
                    returnUserAuthToken(),
                    it,
                    it1,
                ) { result, error ->
                    pBar(0)
                    if (isRefresh)

                        if (error != null) {
                            mOnError(error)
                            return@respondInvite
                        }

                    if (result?.status == true) {
                        successToast(result.message)
                        isRefresh
                        getGameJoins(invitesInvite?.id.toString(), true)
                    }
                }
            }
        }
    }
}
