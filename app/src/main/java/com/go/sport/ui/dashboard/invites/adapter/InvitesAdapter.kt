package com.go.sport.ui.dashboard.invites.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.model.invites.InvitesInvite
import com.go.sport.ui.dashboard.invites.InvitesActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_invites.view.*
import java.util.concurrent.TimeUnit

class InvitesAdapter(
    private val c: Context,
    private val invites: ArrayList<InvitesInvite>,
    private val l: InvitesClickListener
) :
    RecyclerView.Adapter<InvitesAdapter.InvitesAdapterViewHolder>() {
    class InvitesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InvitesAdapterViewHolder {
        return InvitesAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_invites, parent, false)
        )
    }

    override fun onBindViewHolder(holder: InvitesAdapterViewHolder, position: Int) {
        val invite = invites[position]

        holder.itemView.apply {
            val circularProgressDrawable = CircularProgressDrawable(c)
            circularProgressDrawable.centerRadius = 20f
            circularProgressDrawable.start()

            Glide.with(c).load(invite.user?.detail?.profile_image).placeholder(circularProgressDrawable)
                .into(holder.itemView.iv_user)
            Glide.with(c).load(invite.sport?.image).into(holder.itemView.iv_game)
            tv_title.text = invite.title
            tv_date.text = invite.date
            tv_time.text = invite.complete_time
            if (invite.duration == "0") {
                holder.itemView.cont_duration.visibility = View.GONE
            }else{
                tv_duration.text = invite.duration
            }
            tv_user.text = invite.user?.first_name + " " + invite.user?.last_name
            if (invite.user?.detail != null) {
                tv_players.text =
                    "Total: ${invite.detail?.total_players} | Confirmed: ${invite.detail?.confirmed_players}"
                tv_price.text = "PKR ${invite.detail?.cost_per_play}"
                tv_location.text = invite.detail?.address
                tv_count.text = invite.detail?.event_type
                tv_gender.text = invite.user?.detail?.gender?.capitalize()
                tv_age.text = (c as InvitesActivity).getAgeFromDOB(
                    invite.user?.detail?.date_of_birth?.split("-")?.get(0)?.toInt() ?: 0,
                    invite.user?.detail?.date_of_birth?.split("-")?.get(1)?.toInt() ?: 0,
                    invite.user?.detail?.date_of_birth?.split("-")?.get(2)?.toInt() ?: 0
                )
            }

            if (invites[position].invite?.accept_decline == "leave" || invites[position].invite?.accept_decline == "pending" ) {

                join_game.visibility = View.VISIBLE
                leave_game.visibility = View.GONE
            }
            else{
                join_game.visibility = View.GONE
                leave_game.visibility = View.VISIBLE
            }
            RxView.clicks(cont_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onInvitesItemClick(position)
            }

            RxView.clicks(join_game).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onInvitesJoinGameClick(position)
            }

            RxView.clicks(leave_game).throttleFirst(2,TimeUnit.SECONDS).subscribe {
                l.onLeaveGame(position)
            }
        }
    }

    override fun getItemCount(): Int = invites.size

    interface InvitesClickListener {
        fun onInvitesItemClick(position: Int)
        fun onInvitesJoinGameClick(position: Int)
        fun onLeaveGame(position: Int)
    }
}
