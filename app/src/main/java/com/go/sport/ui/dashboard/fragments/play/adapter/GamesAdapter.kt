package com.go.sport.ui.dashboard.fragments.play.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.model.play.PlayData
import com.go.sport.ui.dashboard.DashboardActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_invites.view.*
import kotlinx.android.synthetic.main.row_invites.view.cont_activity
import kotlinx.android.synthetic.main.row_invites.view.iv_game
import kotlinx.android.synthetic.main.row_invites.view.iv_user
import kotlinx.android.synthetic.main.row_invites.view.tv_age
import kotlinx.android.synthetic.main.row_invites.view.tv_date
import kotlinx.android.synthetic.main.row_invites.view.tv_duration
import kotlinx.android.synthetic.main.row_invites.view.tv_gender
import kotlinx.android.synthetic.main.row_invites.view.tv_location
import kotlinx.android.synthetic.main.row_invites.view.tv_players
import kotlinx.android.synthetic.main.row_invites.view.tv_time
import kotlinx.android.synthetic.main.row_invites.view.tv_title
import kotlinx.android.synthetic.main.row_invites.view.tv_user
import kotlinx.android.synthetic.main.row_my_games.view.*
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class GamesAdapter(
    private val c: Context,
    private val plays: ArrayList<PlayData>,
    private val l: GamesClickListener
) :
    RecyclerView.Adapter<GamesAdapter.InvitesAdapterViewHolder>() {
    class InvitesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InvitesAdapterViewHolder {
        return InvitesAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_invites, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: InvitesAdapterViewHolder, position: Int) {
        val play = plays[position]

        holder.itemView.apply {
            val circularProgressDrawable = CircularProgressDrawable(c)
            circularProgressDrawable.centerRadius = 20f
            circularProgressDrawable.start()

            if (play.user.detail.profile_image != "") {
                Glide.with(c).load(play.user.detail.profile_image)
                    .placeholder(circularProgressDrawable)
                    .into(holder.itemView.iv_user)
            } else {
                Glide.with(c).load(R.drawable.avatar).placeholder(circularProgressDrawable)
                    .into(holder.itemView.iv_user)
            }
            Glide.with(c).load(play.sport.image).into(holder.itemView.iv_game)
            tv_count.text = play.join_count
            tv_title.text = play.title
            tv_location.text = play.detail.address
            tv_date.text = play.date
            if (play.detail.start_timing_out == "N/A" && play.detail.end_timing_out == "N/A") {
                tv_time.text = play.complete_time
            } else {
                tv_time.text = play.detail.start_timing_out
                    .toUpperCase() + " - " + play.detail.end_timing_out.toUpperCase()
            }
            val timeFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

            if (play.detail?.end_timing_out != "N/A" && play.detail?.start_timing_out != "N/A") {
                val endTime = play.detail?.end_timing_out
                val startTime = play.detail?.start_timing_out

                val start: LocalTime =
                    LocalTime.parse(startTime.toUpperCase(), timeFormatter)
                val end: LocalTime =
                    LocalTime.parse(endTime.toUpperCase(), timeFormatter)

                val diff: Duration = Duration.between(start, end)

                val hours: Long = diff.toHours()
                val minutes: Long = diff.minusHours(hours).toMinutes()
                holder.itemView.tv_duration.text = ("$hours Hours $minutes Minutes")
            }  else  {
                holder.itemView.tv_duration.text = play.duration
            }

            tv_players.text =
                "Total: ${play.detail.total_players} | Confirmed: ${play.detail.confirmed_players}"
            if (play.detail.game_fee != "0") {
                tv_price.text = "${play.currency} ${play.detail.game_fee}"
            } else if (play.slot.isNotEmpty()) {
                tv_price.text = "${play.currency} ${play.slot[0].slot_price}"
            } else {
                tv_price.text = "${play.currency} ${play.detail.cost_per_play}"
            }
            tv_user.text = play.user.first_name + " " + play.user.last_name
            if (play.user.detail != null) {
                tv_gender.text = play.user.detail.gender.capitalize()
                tv_age.text = (c as DashboardActivity).getAgeFromDOB(
                    play.user.detail.date_of_birth.split("-")[0].toInt(),
                    play.user.detail.date_of_birth.split("-")[1].toInt(),
                    play.user.detail.date_of_birth.split("-")[2].toInt()
                )
                if (play.joined == true) {
                    join_game.visibility = View.GONE
                    leave_game.visibility = View.VISIBLE
                } else {
                    leave_game.visibility = View.GONE
                    join_game.visibility = View.VISIBLE
                }
            }

            RxView.clicks(cont_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onGamesItemClick(play)
            }

            RxView.clicks(tv_join_game).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onGamesJoinGameClick(position)
            }
            RxView.clicks(leave_game).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onLeaveGame(position)
            }
        }
    }

    override fun getItemCount(): Int = plays.size

    interface GamesClickListener {
        fun onGamesItemClick(play: PlayData)
        fun onGamesJoinGameClick(position: Int)
        fun onLeaveGame(position: Int)
    }
}
