package com.go.sport.ui.dashboard.fragments.home.adapter

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
import com.go.sport.model.mygames.Data
import com.go.sport.ui.dashboard.DashboardActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.row_my_games.view.*
import kotlinx.android.synthetic.main.row_my_games.view.tv_age
import kotlinx.android.synthetic.main.row_my_games.view.tv_gender
import kotlinx.android.synthetic.main.row_my_games.view.tv_user
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

class MyGamesAdapter(
    private val c: Context,
    private val myGames: ArrayList<Data>,
    private val onMyGameClick: (Data) -> Unit
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
        val view = MyGamesAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_my_games, parent, false)
        )

        view.itemView.layoutParams.width = (rv.measuredWidth / 1.1).toInt()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: MyGamesAdapterViewHolder, position: Int) {
        val myGame = myGames[position]

        val circularProgressDrawable = CircularProgressDrawable(c)
        circularProgressDrawable.centerRadius = 20f
        circularProgressDrawable.start()



        if (myGame.type == "booking") {
            holder.itemView.cont_booking.visibility = View.VISIBLE
            holder.itemView.tv_players.visibility = View.GONE
            holder.itemView.tv_status.text = "Booking"
        } else {
            holder.itemView.cont_booking.visibility = View.GONE
            holder.itemView.tv_status.text = "Activity"
        }

        Glide.with(c).load(myGame.user?.detail?.profileImage).placeholder(circularProgressDrawable)
            .into(holder.itemView.iv_user)
        Glide.with(c).load(myGame.sport?.image).into(holder.itemView.iv_game)
        holder.itemView.tv_title.text = myGame.title!!.capitalize()
        holder.itemView.tv_date.text = if (!myGame.date.isNullOrEmpty()) myGame.date else "yes"


        if (myGame.type == "out_of_app_activity") {
            holder.itemView.tv_time.text =
                myGame.detail?.startTimingOut.toString().toUpperCase() + " - " + myGame.detail?.endTimingOut.toString().toUpperCase()
            holder.itemView.tv_title.text = myGame.title.capitalize()

            val timeFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

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
        } else {
            holder.itemView.tv_time.text = myGame.completeTime
            holder.itemView.tv_duration.text = myGame.duration
        }


        holder.itemView.tv_location.text = if (!myGame.facility?.address.isNullOrEmpty()) {
            myGame.facility?.address
        } else {
            myGame.detail?.address
        }
        holder.itemView.tv_pitch_size.text = myGame.facility?.pitchsize
        holder.itemView.tv_booking_id.text = myGame.bookingId
        if (myGame.detail != null) {
            holder.itemView.tv_players.text =
                "Total: ${myGame.detail.totalPlayers} | Confirmed: ${myGame.detail.confirmedPlayers}"
        }
//            holder.itemView.tv_price.text = "PKR ${myGame.myBookingDetail.game_fee}"
        holder.itemView.tv_user.text = myGame.user?.firstName + " " + myGame.user?.lastName
        if (myGame.user?.detail != null) {
            holder.itemView.tv_gender.text = myGame.user.detail.gender.capitalize()
            holder.itemView.tv_age.text = (c as DashboardActivity).getAgeFromDOB(
                myGame.user.detail.dateOfBirth?.split("-")?.get(0)?.toInt() ?: 0,
                myGame.user.detail.dateOfBirth?.split("-")?.get(1)?.toInt() ?: 0,
                myGame.user.detail.dateOfBirth?.split("-")?.get(2)?.toInt() ?: 0
            ) + " Years"
        }

        RxView.clicks(holder.itemView).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            onMyGameClick(myGame)
        }
    }

    override fun getItemCount(): Int = myGames.size

    /*interface MyGamesClickListener {
        fun onMyGamesItemClick(position: Int)
    }*/
}
