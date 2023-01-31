package com.go.sport.ui.dashboard.fragments.home.adapter

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
import com.go.sport.model.mygames.Data
import com.go.sport.ui.dashboard.fragments.home.mygames.MyGamesActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_my_booking.view.*
import kotlinx.android.synthetic.main.row_my_games.view.*
import kotlinx.android.synthetic.main.row_my_games.view.cont_booking
import kotlinx.android.synthetic.main.row_my_games.view.iv_game
import kotlinx.android.synthetic.main.row_my_games.view.iv_user
import kotlinx.android.synthetic.main.row_my_games.view.tv_age
import kotlinx.android.synthetic.main.row_my_games.view.tv_booking_id
import kotlinx.android.synthetic.main.row_my_games.view.tv_date
import kotlinx.android.synthetic.main.row_my_games.view.tv_duration
import kotlinx.android.synthetic.main.row_my_games.view.tv_gender
import kotlinx.android.synthetic.main.row_my_games.view.tv_location
import kotlinx.android.synthetic.main.row_my_games.view.tv_pitch_size
import kotlinx.android.synthetic.main.row_my_games.view.tv_time
import kotlinx.android.synthetic.main.row_my_games.view.tv_title
import kotlinx.android.synthetic.main.row_my_games.view.tv_user
import java.util.concurrent.TimeUnit

class MybookingAdapter(
    private val c: Context,
    private val myBookings: ArrayList<Data>,
    private val l: MyBookingClickListener
) :
    RecyclerView.Adapter<MybookingAdapter.MybookingAdapterViewHolder>() {
    class MybookingAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var rv: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rv = recyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MybookingAdapterViewHolder {
        val view = MybookingAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_my_booking, parent, false)
        )

        return view
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: MybookingAdapterViewHolder, position: Int) {
        val myGame = myBookings[position]

        val circularProgressDrawable = CircularProgressDrawable(c)
        circularProgressDrawable.centerRadius = 20f
        circularProgressDrawable.start()

            holder.itemView.cont_booking.visibility = View.VISIBLE

        Glide.with(c).load(myGame.user?.detail?.profileImage).placeholder(circularProgressDrawable)
            .into(holder.itemView.ic_user)
        Glide.with(c).load(myGame.sport?.image).into(holder.itemView.ic_game)
        holder.itemView.title.text = myGame.title
        holder.itemView.date.text = myGame.date
        holder.itemView.time.text = myGame.completeTime
        holder.itemView.duration.text = myGame.duration.toString()
        holder.itemView.status.text = "Booking"
        holder.itemView.tv_address.text = myGame.facility?.address
        holder.itemView.pitch_size.text = myGame.facility?.pitchsize
        holder.itemView.tv_booking_id.text = myGame.bookingId
        if (myGame.detail != null) {
            /*holder.itemView.tv_players.text =
                "Total: ${myGame.myBookingDetail.totalPlayers} | Confirmed: ${myGame.myBookingDetail.confirmedPlayers}"*/
        }
//            holder.itemView.tv_price.text = "PKR ${myGame.myBookingDetail.game_fee}"
        holder.itemView.user.text = myGame.user?.firstName?.capitalize() + " " + myGame.user?.lastName?.capitalize()
        if (myGame.user?.detail != null) {
            holder.itemView.gender.text = myGame.user.detail.gender.capitalize()
            holder.itemView.age.text = (c as BaseActivity).getAgeFromDOB(
                myGame.user.detail.dateOfBirth?.split("-")?.get(0)?.toInt() ?: 0,
                myGame.user.detail.dateOfBirth?.split("-")?.get(1)?.toInt() ?: 0,
                myGame.user.detail.dateOfBirth?.split("-")?.get(2)?.toInt() ?: 0
            )
        }

        RxView.clicks(holder.itemView).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            l.onMyBookingItemClick(position)
        }
    }

    override fun getItemCount(): Int = myBookings.size

    interface MyBookingClickListener {
        fun onMyBookingItemClick(position: Int)
    }
}
