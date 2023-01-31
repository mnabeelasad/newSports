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
import com.go.sport.model.fundrequest.received.ReceivedFundRequestsData
import com.go.sport.model.fundrequest.received.ReceivedFundRequestsSport
import com.go.sport.ui.dashboard.fragments.home.requestfunds.RequestFundsActivity
import kotlinx.android.synthetic.main.row_fundsrequests.view.*
import java.util.*

class FundsRequestsAdapter(
    private val c: Context,
    private val members: ArrayList<ReceivedFundRequestsData>,
    val l: OnFundsRequests,
) :
    RecyclerView.Adapter<FundsRequestsAdapter.FundRequestsAdapterViewHolder>() {
    class FundRequestsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FundRequestsAdapterViewHolder {
        return FundRequestsAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_fundsrequests, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: FundRequestsAdapterViewHolder, position: Int) {
        holder.itemView.apply {
            rv_sports.apply {
                layoutManager = LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false)
                adapter = UserSportsAdapter(
                    c,
                    members[position].user.sport as ArrayList<ReceivedFundRequestsSport>
                )
            }

            val circularProgressDrawable = CircularProgressDrawable(c)
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            tv_user.text = members[position].user.firstName + " " + members[position].user.lastName
            tv_date.text = members[position].dateAppended
            tv_time.text = members[position].timeAppended
            tv_desc.text = members[position].description
            tv_amount.text = "PKR ${members[position].amount}"

            if (members[position].user.detail != null) {
                Glide.with(c).load(members[position].user.detail.profileImage)
                    .placeholder(circularProgressDrawable).into(iv_image)
                tv_gender.text =
                    members[position].user.detail.gender.capitalize(Locale.getDefault())
                tv_age.text = (c as RequestFundsActivity).getAgeFromDOB(
                    members[position].user.detail.dateOfBirth.split("-")[0].toInt(),
                    members[position].user.detail.dateOfBirth.split("-")[1].toInt(),
                    members[position].user.detail.dateOfBirth.split("-")[2].toInt()
                )
            }

            tv_accept.setOnClickListener {
                l.onAcceptClick(position)
            }

            tv_decline.setOnClickListener {
                l.onDeclineClick(position)
            }
        }
    }

    override fun getItemCount(): Int = members.size
}

interface OnFundsRequests {
    fun onAcceptClick(pos: Int)
    fun onDeclineClick(pos: Int)
}