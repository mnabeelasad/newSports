package com.go.sport.ui.dashboard.fragments.home.leaderboard.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.model.leaderboardnew.LeaderboardData
import com.go.sport.ui.dashboard.fragments.home.leaderboard.LeaderBoardActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_leader_board.view.*
import java.util.concurrent.TimeUnit

class LeaderBoardAdapter(
    private val c: Context,
    private val leaderBoardUsers: ArrayList<LeaderboardData>,
    private val l: LeaderBoardAdapterClickListener
) :
    RecyclerView.Adapter<LeaderBoardAdapter.MyGamesAdapterViewHolder>() {
    class MyGamesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyGamesAdapterViewHolder {

        return MyGamesAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_leader_board, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: MyGamesAdapterViewHolder, position: Int) {
        val user = leaderBoardUsers[position]

        val circularProgressDrawable = CircularProgressDrawable(c)
        circularProgressDrawable.centerRadius = 15f
        circularProgressDrawable.start()

        holder.itemView.apply {
            tv_rank.text = "# " + (position + 4).toString()
            tv_user.text = user.first_name.capitalize() + " " + user.last_name.capitalize()
            if (user.detail != null) {
                Glide.with(this).load(user.detail.profile_image)
                    .placeholder(circularProgressDrawable).into(iv_user)
                tv_gender.text = user.detail.gender.capitalize()
                tv_age.text = (c as LeaderBoardActivity).getAgeFromDOB(
                    user.detail.date_of_birth.split("-")[0].toInt(),
                    user.detail.date_of_birth.split("-")[1].toInt(),
                    user.detail.date_of_birth.split("-")[2].toInt()
                )
            }
            tv_points.text = user.reward_points
            tv_level.text = user.level.toString()

            RxView.clicks(cont_root).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onLeaderBoardItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int = leaderBoardUsers.size - 3

    interface LeaderBoardAdapterClickListener {
        fun onLeaderBoardItemClick(position: Int)
    }
}
