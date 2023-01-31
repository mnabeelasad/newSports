package com.go.sport.ui.dashboard.fragments.home.requestfunds.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.model.fundrequest.received.ReceivedFundRequestsSport
import com.go.sport.model.getusers.GetUserSport
import kotlinx.android.synthetic.main.row_user_sports.view.*

class UserSportsAdapter(
    private val c: Context,
    private val sports: ArrayList<ReceivedFundRequestsSport>
) :
    RecyclerView.Adapter<UserSportsAdapter.UserSportsAdapterViewHolder>() {
    class UserSportsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSportsAdapterViewHolder {

        return UserSportsAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_user_sports, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: UserSportsAdapterViewHolder, position: Int) {
        val sport = sports[position]
        holder.itemView.apply {
            Glide.with(c).load(sport.image).into(iv_sports)
        }
    }

    override fun getItemCount(): Int = sports.size
}