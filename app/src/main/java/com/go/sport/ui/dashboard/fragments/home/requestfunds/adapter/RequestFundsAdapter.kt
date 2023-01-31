package com.go.sport.ui.dashboard.fragments.home.requestfunds.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.model.getusers.GetUserSport
import com.go.sport.model.getusers.GetUsersData
import com.go.sport.ui.dashboard.fragments.home.adapter.UserSportsAdapter
import com.go.sport.ui.dashboard.fragments.home.requestfunds.RequestFundsActivity
import kotlinx.android.synthetic.main.row_requestfunds.view.*
import java.util.*

class RequestFundsAdapter(
    private val c: Context,
    private val members: ArrayList<GetUsersData>,
    val l: OnRequestFunds,
) :
    RecyclerView.Adapter<RequestFundsAdapter.RequestFundsAdapterViewHolder>() {
    class RequestFundsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestFundsAdapterViewHolder {
        return RequestFundsAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_requestfunds, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RequestFundsAdapterViewHolder, position: Int) {
        holder.itemView.apply {
            rv_sports.apply {
                layoutManager = LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false)
                adapter = UserSportsAdapter(c, members[position].sport as ArrayList<GetUserSport>)
            }

            val circularProgressDrawable = CircularProgressDrawable(c)
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            tv_user.text = members[position].first_name + " " + members[position].last_name

            if (members[position].detail != null) {
                Glide.with(c).load(members[position].detail.profile_image)
                    .placeholder(circularProgressDrawable).into(iv_image)
                tv_gender.text = members[position].detail.gender.capitalize(Locale.getDefault())
                tv_age.text = (c as RequestFundsActivity).getAgeFromDOB(
                    members[position].detail.date_of_birth.split("-")[0].toInt(),
                    members[position].detail.date_of_birth.split("-")[1].toInt(),
                    members[position].detail.date_of_birth.split("-")[2].toInt()
                )
            }

            tv_request.setOnClickListener {
                l.onRequestClick(position)
            }
        }
    }

    override fun getItemCount(): Int = members.size
}

interface OnRequestFunds {
    fun onRequestClick(pos: Int)
}