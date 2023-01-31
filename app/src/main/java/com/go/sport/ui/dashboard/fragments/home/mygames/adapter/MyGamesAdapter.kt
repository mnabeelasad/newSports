package com.go.sport.ui.dashboard.fragments.home.mygames.adapter

import android.annotation.SuppressLint
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
import com.go.sport.base.BaseActivity
import com.go.sport.model.mygames.Data
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.dashboard.fragments.home.mygames.MyGamesActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_my_games.view.*
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MyGamesAdapter(
    private val c: Context,
    private val myGames: ArrayList<Data>,
    private val l: MyGamesClickListener
) :
    RecyclerView.Adapter<MyGamesAdapter.MyGamesAdapterViewHolder>() {
    class MyGamesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var rv: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rv = recyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyGamesAdapterViewHolder {
        val view =
            MyGamesAdapterViewHolder(
                LayoutInflater.from(c).inflate(R.layout.row_my_games, parent, false)
            )

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: MyGamesAdapterViewHolder, position: Int) {
        val myGame = myGames[position]

        val circularProgressDrawable = CircularProgressDrawable(c)
        circularProgressDrawable.centerRadius = 20f
        circularProgressDrawable.start()


        Glide.with(c).load(myGame.user!!.detail.profileImage).placeholder(circularProgressDrawable)
            .into(holder.itemView.iv_user)
        Glide.with(c).load(myGame.sport?.image).into(holder.itemView.iv_game)
        holder.itemView.tv_title.text = myGame.title
        holder.itemView.tv_date.text = myGame.date

        holder.itemView.tv_duration.text = myGame.duration.toString()
        holder.itemView.tv_status.text = "Activity"

        if (myGame.type == "out_of_app_activity") {
            holder.itemView.tv_location.text = myGame.detail?.address
            holder.itemView.tv_time.text =
                myGame.detail?.startTimingOut.toString().toUpperCase() + " - " + myGame.detail?.endTimingOut.toString().toUpperCase()
            val timeFormatter =  if(myGame.detail?.startTimingOut.toString().toLowerCase().contains("am") || myGame.detail?.startTimingOut.toString().toLowerCase().contains("pm"))
                DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
            else
                DateTimeFormatter.ofPattern("hh:mm:ss", Locale.ENGLISH)

            if (myGame.detail?.endTimingOut != "N/A" && myGame.detail?.startTimingOut != "N/A") {
                val endTime = myGame.detail?.endTimingOut
                val startTime = myGame.detail?.startTimingOut

                val start: LocalTime = LocalTime.parse(startTime.toString().toUpperCase(), timeFormatter)
                val end: LocalTime = LocalTime.parse(endTime.toString().toUpperCase(), timeFormatter)

                val diff: Duration = Duration.between(start, end)

                val hours: Long = diff.toHours()
                val minutes: Long = diff.minusHours(hours).toMinutes()
                holder.itemView.tv_duration.text = ("$hours Hours $minutes Minutes")
            } else if (myGame.detail.endTimingOut == "N/A") {
                holder.itemView.tv_duration.text = myGame.detail?.startTimingOut + " - " + "N/A"
            } else if (myGame.detail.startTimingOut == "N/A") {
                holder.itemView.tv_duration.text = "N/A" + " - " + myGame.detail?.endTimingOut
            }
            holder.itemView.tv_pitch_size.text = myGame.detail?.pitchCourt
        } else {
            holder.itemView.tv_location.text = myGame.facility?.address
            holder.itemView.tv_time.text = myGame.completeTime
            holder.itemView.tv_pitch_size.text = myGame.facility?.pitchsize
        }

//        holder.itemView.tv_booking_id.text = myGame.bookingId
        if (myGame.detail != null) {
            holder.itemView.tv_players.text =
                "Total: ${myGame.detail.totalPlayers} | Confirmed: ${myGame.detail.confirmedPlayers}"
        }

        if (myGame.detail != null) {
            holder.itemView.tv_players.text =
                "Total Players: ${myGame.detail?.totalPlayers} | Confirmed Players: ${myGame.detail?.confirmedPlayers}"
        }
//            holder.itemView.tv_price.text = "PKR ${myGame.myBookingDetail.game_fee}"
        holder.itemView.tv_user.text = myGame.user?.firstName + " " + myGame.user?.lastName
        if (myGame.user?.detail != null) {
            holder.itemView.tv_gender.text = myGame.user.detail.gender.capitalize()
            holder.itemView.tv_age.text = (c as BaseActivity).getAgeFromDOB(
                myGame.user.detail.dateOfBirth?.split("-")?.get(0)?.toInt() ?: 0,
                myGame.user.detail.dateOfBirth?.split("-")?.get(1)?.toInt() ?: 0,
                myGame.user.detail.dateOfBirth?.split("-")?.get(2)?.toInt() ?: 0
            )
        }

        RxView.clicks(holder.itemView).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            l.onMyGamesItemClick(position)
        }
    }

    override fun getItemCount(): Int = myGames.size

    interface MyGamesClickListener {
        fun onMyGamesItemClick(position: Int)
    }
}
