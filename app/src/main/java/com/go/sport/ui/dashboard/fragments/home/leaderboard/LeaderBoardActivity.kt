package com.go.sport.ui.dashboard.fragments.home.leaderboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.leaderboardnew.LeaderboardData
import com.go.sport.model.leaderboardnew.PlayerFirst
import com.go.sport.model.leaderboardnew.PlayerSecond
import com.go.sport.model.leaderboardnew.PlayerThird
import com.go.sport.network.APIManager
import com.go.sport.ui.dashboard.fragments.home.leaderboard.adapter.LeaderBoardAdapter
import com.go.sport.ui.dashboard.playerprofile.PlayerProfileActivity
import com.go.sport.ui.dashboard.termsconditions.TermsAndConditionsActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_leader_board.*
import java.util.concurrent.TimeUnit

class LeaderBoardActivity : BaseActivity(),
    LeaderBoardAdapter.LeaderBoardAdapterClickListener {

    private val leaderBoardUsers = arrayListOf<LeaderboardData>()
    private lateinit var leaderBoardTopper: PlayerFirst
    private lateinit var leaderBoardSecond: PlayerSecond
    private lateinit var leaderBordThird: PlayerThird

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader_board)

        initViews()
        initTabs()
        initListeners()
        mGetLeaderBoard()
    }

    private fun initViews() {
        rv_leader_board.adapter = LeaderBoardAdapter(this, leaderBoardUsers, this)
    }

    private fun initTabs() {
        tab_layout.visibility = View.GONE
        tab_layout.addTab(tab_layout.newTab().setText("Fellow Users"))
        tab_layout.addTab(tab_layout.newTab().setText("Overall Users"))
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(cont_first).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivity(
                this,
                PlayerProfileActivity::class.java,
                false,
                1,
                bundleOf(Pair("user_id", leaderBoardTopper.detail.user_id))
            )
        }

        RxView.clicks(cont_second).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivity(
                this,
                PlayerProfileActivity::class.java,
                false,
                1,
                bundleOf(Pair("user_id", leaderBoardSecond.detail.user_id))
            )
        }

        RxView.clicks(cont_third).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivity(
                this,
                PlayerProfileActivity::class.java,
                false,
                1,
                bundleOf(Pair("user_id", leaderBordThird.detail.user_id))
            )
        }

        RxView.clicks(terms).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivity(this, TermsAndConditionsActivity::class.java, false, 1)
        }

    }

    private fun mGetLeaderBoard() {
        pBar(1)
        APIManager.getLeaderBoard(returnUserAuthToken()) { result, error ->
            pBar(0)
            leaderBoardUsers.clear()

            if (error != null) {
                mOnError(error)
                return@getLeaderBoard
            }

            if (result?.status == true) {
                leaderBoardUsers.addAll(result.data)
                leaderBoardTopper = (result.first)
                leaderBoardSecond = (result.second)
                leaderBordThird = (result.third)

                rv_leader_board.adapter?.notifyDataSetChanged()

                val circularProgressDrawable = CircularProgressDrawable(this)
                circularProgressDrawable.centerRadius = 15f
                circularProgressDrawable.start()

                tv_user_one.text =
                    leaderBoardTopper.first_name.capitalize() + " " + leaderBoardTopper.last_name.capitalize()
                if (leaderBoardTopper.detail != null) {
                    Glide.with(this).load(leaderBoardTopper.detail.profile_image)
                        .placeholder(circularProgressDrawable).into(iv_user_one)
                    tv_gender_one.text = leaderBoardTopper.detail.gender.capitalize()
                    tv_age_one.text = getAgeFromDOB(
                        leaderBoardTopper.detail.date_of_birth.split("-")[0].toInt(),
                        leaderBoardTopper.detail.date_of_birth.split("-")[1].toInt(),
                        leaderBoardTopper.detail.date_of_birth.split("-")[2].toInt()
                    ) + " Years"
                }
                tv_points_one.text = leaderBoardTopper.reward_points
                tv_level_one.text = leaderBoardTopper.level.toString().capitalize()

                tv_user_two.text =
                    leaderBordThird.first_name.capitalize() + " " + leaderBordThird.last_name.capitalize()
                if (leaderBordThird.detail != null) {
                    Glide.with(this).load(leaderBordThird.detail.profile_image)
                        .placeholder(circularProgressDrawable).into(iv_user_two)
                    tv_gender_two.text = leaderBordThird.detail.gender.capitalize()
                    tv_age_two.text = getAgeFromDOB(
                        leaderBordThird.detail.date_of_birth.split("-")[0].toInt(),
                        leaderBordThird.detail.date_of_birth.split("-")[1].toInt(),
                        leaderBordThird.detail.date_of_birth.split("-")[2].toInt()
                    ) + " Years"
                }
                tv_points_two.text = leaderBordThird.reward_points
                tv_level_two.text = leaderBordThird.level.toString().capitalize()

                tv_user_three.text =
                    leaderBoardSecond.first_name.capitalize() + " " + leaderBoardSecond.last_name.capitalize()
                if (leaderBoardSecond.detail != null) {
                    Glide.with(this).load(leaderBoardSecond.detail.profile_image)
                        .placeholder(circularProgressDrawable).into(iv_user_three)
                    tv_gender_three.text = leaderBoardSecond.detail.gender.capitalize()
                    tv_age_three.text = getAgeFromDOB(
                        leaderBoardSecond.detail.date_of_birth.split("-")[0].toInt(),
                        leaderBoardSecond.detail.date_of_birth.split("-")[1].toInt(),
                        leaderBoardSecond.detail.date_of_birth.split("-")[2].toInt()
                    ) + " Years"
                }
                tv_points_three.text = leaderBoardSecond.reward_points
                tv_level_three.text = leaderBoardSecond.level.toString().capitalize()

            }
        }
    }

    override fun onLeaderBoardItemClick(position: Int) {
        startActivity(
            this,
            PlayerProfileActivity::class.java,
            false,
            1,
            bundleOf(Pair("user_id", leaderBoardUsers[position].detail.user_id))
        )
    }
}

