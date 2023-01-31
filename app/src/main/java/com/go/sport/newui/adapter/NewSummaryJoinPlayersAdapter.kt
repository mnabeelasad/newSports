package com.go.sport.newui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.model.gamejoins.GameJoinsData
import com.go.sport.newui.NewSummaryScreen
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.dashboard.summary.adapter.SummaryPlayersAdapter
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_joined_players.view.*
import kotlinx.android.synthetic.main.row_joined_players.view.iv_profile_image
import kotlinx.android.synthetic.main.row_summary_players.view.*
import java.util.concurrent.TimeUnit

class NewSummaryJoinPlayersAdapter(
    private val c: Context,
    private val players: List<GameJoinsData>,
    private val l: SummaryPlayersClickListener
) :
    RecyclerView.Adapter<NewSummaryJoinPlayersAdapter.NewSummaryJoinPlayersAdapterViewHolder>() {
    class NewSummaryJoinPlayersAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var rv: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rv = recyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewSummaryJoinPlayersAdapterViewHolder {
        return NewSummaryJoinPlayersAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_summary_players, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: NewSummaryJoinPlayersAdapterViewHolder, position: Int) {
        val player = players[position]

        holder.itemView.apply {
            tv_id.text = (position + 1).toString() + "."
            val circularProgressDrawable = CircularProgressDrawable(c)
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            tv_user_name.text = player.user.firstName + " " + player.user.lastName
            if (player.user.detail != null) {
                if (player.user.detail.profileImage != "") {
                    Glide.with(c).load(player.user.detail.profileImage)
                        .placeholder(circularProgressDrawable).into(iv_image)
                }else{
                    Glide.with(c).load(R.drawable.avatar)
                        .placeholder(circularProgressDrawable).into(iv_image)
                }
                tv_gender.text = player.user.detail.gender.capitalize()
                tv_age.text = (c as NewSummaryScreen).getAgeFromDOB(
                    player.user.detail.dateOfBirth.split("-")[0].toInt(),
                    player.user.detail.dateOfBirth.split("-")[1].toInt(),
                    player.user.detail.dateOfBirth.split("-")[2].toInt()
                ) + "Years"
            }
        }

        RxView.clicks(holder.itemView).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            l.onSummaryJoinedPlayersItemClick(position)
        }
    }

    override fun getItemCount(): Int = players.size

    interface SummaryPlayersClickListener {
        fun onSummaryJoinedPlayersItemClick(position: Int)
    }
}
