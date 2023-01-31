package com.go.sport.ui.dashboard.summary.adapter

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
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_joined_players.view.*
import kotlinx.android.synthetic.main.row_summary_players.view.*
import java.util.concurrent.TimeUnit

class SummaryPlayersAdapter(
    private val c: Context,
    private val players: List<GameJoinsData>,
    private val l: SummaryPlayersClickListener
) :
    RecyclerView.Adapter<SummaryPlayersAdapter.SummaryPlayersAdapterViewHolder>() {
    class SummaryPlayersAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var rv: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rv = recyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SummaryPlayersAdapterViewHolder {
        return SummaryPlayersAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_joined_players, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: SummaryPlayersAdapterViewHolder, position: Int) {
        val player = players[position]

        holder.itemView.apply {
            val circularProgressDrawable = CircularProgressDrawable(c)
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            tv_name.text = player.user.firstName + " " + player.user.lastName[0]
            if (player.user.detail != null) {
                if (player.user.detail.profileImage == "") {
                    Glide.with(c).load(R.drawable.avatar)
                        .placeholder(circularProgressDrawable).into(iv_profile_image)
                } else {
                    Glide.with(c).load(player.user.detail.profileImage)
                        .placeholder(circularProgressDrawable).into(iv_profile_image)
                }
//                tv_status.text = player.acceptDecline
            }
        }

        RxView.clicks(holder.itemView).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            l.onSummaryPlayersItemClick(position)
        }
    }

    override fun getItemCount(): Int = players.size

    interface SummaryPlayersClickListener {
        fun onSummaryPlayersItemClick(position: Int)
    }
}
