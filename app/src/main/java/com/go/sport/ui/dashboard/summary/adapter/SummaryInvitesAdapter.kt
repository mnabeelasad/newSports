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
import com.go.sport.base.BaseActivity
import com.go.sport.model.gameinvites.GameInvitesData
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_joined_players.view.*
import kotlinx.android.synthetic.main.row_summary_players.view.*
import kotlinx.android.synthetic.main.row_summary_players.view.iv_image
import java.util.concurrent.TimeUnit

class SummaryInvitesAdapter(
    private val c: Context,
    private val invites: List<GameInvitesData>,
    private val l: SummaryInvitesClickListener
) :
    RecyclerView.Adapter<SummaryInvitesAdapter.SummaryPlayersAdapterViewHolder>() {
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
        val invite = invites[position]

        holder.itemView.apply {
            val circularProgressDrawable = CircularProgressDrawable(c)
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            if(invite.user.lastName.isNotEmpty())
            tv_name.text = invite.user.firstName + " " + invite.user.lastName[0]
            else
            tv_name.text = invite.user.firstName
            if (invite.user.detail != null) {
                Glide.with(c).load(invite.user.detail.profileImage)
                    .placeholder(circularProgressDrawable).into(iv_profile_image)
            }
        }

        RxView.clicks(holder.itemView).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            l.onSummaryInvitesItemClick(position)
        }
    }

    override fun getItemCount(): Int = invites.size

    interface SummaryInvitesClickListener {
        fun onSummaryInvitesItemClick(position: Int)
    }
}
