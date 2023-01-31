package com.go.sport.ui.dashboard.invites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.invites.InvitesInvite
import com.go.sport.network.APIManager
import com.go.sport.newui.NewSummaryScreen
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.invites.adapter.InvitesAdapter

import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_invites.*
import kotlinx.android.synthetic.main.activity_invites.iv_back
import kotlinx.android.synthetic.main.activity_my_games.*
import kotlinx.android.synthetic.main.row_invites.*
import kotlinx.android.synthetic.main.row_invites.view.*

class InvitesActivity : BaseActivity(),
    InvitesAdapter.InvitesClickListener {

    private val invites = arrayListOf<InvitesInvite>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invites)

        refresh()
        initViews()
        initListeners()
        getInvites()

    }

    private fun refresh() {
        refresh_invites.setOnRefreshListener {
            getInvites(true)
            refresh_invites.setRefreshing(false)
        }
    }

    private fun initViews() {
        rv_invites.apply {
            adapter = InvitesAdapter(
                this@InvitesActivity,
                invites,
                this@InvitesActivity
            )
        }
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }
    }

    private fun getInvites(isRefresh: Boolean = false) {
        pBar(1)
        APIManager.allInvites(returnUserAuthToken()) { result, error ->
            pBar(0)
            if (isRefresh)
                invites.clear()

            if (error != null) {
                mOnError(error)
                return@allInvites
            }

            if (result?.status == true) {
                invites.addAll(result.Invites)
                rv_invites.adapter?.notifyDataSetChanged()
                invites.reverse()
                if (result.Invites.isEmpty()) {
                    errorToast("No invites found!")
                    return@allInvites
                }

            }
        }
    }

    private fun respondToInvite(inviteId: String, gameId: String, isRefresh: Boolean = false) {
        pBar(1)
        APIManager.respondInvite(returnUserAuthToken(), inviteId, gameId) { result, error ->
            pBar(0)
            if (isRefresh)
                invites.clear()

            if (error != null) {
                mOnError(error)
                return@respondInvite
            }

            if (result?.status == true) {
                successToast(result.message)
                join_game.visibility = View.GONE
                leave_game.visibility = View.VISIBLE
                getInvites()
            }
        }
    }


    private fun leaveGame(inviteId: String, gameId: String) {
        pBar(1)
        APIManager.leaveGame(returnUserAuthToken(), inviteId, gameId) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@leaveGame
            }
            if (result?.status == true) {
                if (result.message == "Left game") {
                    invites.clear()
                    successToast("Game Left Successfully")
                    join_game.visibility = View.VISIBLE
                    leave_game.visibility = View.GONE
                    getInvites()
                }
            }
        }
    }


    override fun onInvitesItemClick(position: Int) {
        Singleton.invitesInvite = invites[position]
        startActivity(this, NewSummaryScreen::class.java, false, 1)
    }

    override fun onInvitesJoinGameClick(position: Int) {
        if (invites[position].invite?.accept_decline == "leave" || invites[position].invite?.accept_decline == "pending")
            respondToInvite(
                invites[position].invite?.id.toString(),
                invites[position].id.toString()
            )
    }

    override fun onLeaveGame(position: Int) {
        if (invites[position].invite?.accept_decline == "accept")
            leaveGame(
                invites[position].invite?.id.toString(),
                invites[position].detail?.game_id.toString()
            )
    }
}