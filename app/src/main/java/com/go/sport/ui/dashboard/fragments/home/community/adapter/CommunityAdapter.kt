package com.go.sport.ui.dashboard.fragments.home.community.adapter

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
import com.go.sport.ui.dashboard.fragments.home.community.CommunityActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_community.view.*
import java.util.concurrent.TimeUnit

class CommunityAdapter(
    private val c: Context,
    private val l: CommunityAdapterClickListener,
    private val members: ArrayList<GetUsersData>,
    private val mode: String
) :
    RecyclerView.Adapter<CommunityAdapter.CommunityAdapterViewHolder>() {
    class CommunityAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityAdapterViewHolder {
        return CommunityAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_community, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: CommunityAdapterViewHolder, position: Int) {
        val member = members[position]

        holder.itemView.apply {
            rv_sports.apply {
                layoutManager = LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false)
                adapter = UserSportsAdapter(c, members[position].sport as ArrayList<GetUserSport>)
            }

            val circularProgressDrawable = CircularProgressDrawable(c)
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            if (member.detail != null) {
                Glide.with(c).load(member.detail.profile_image)
                    .placeholder(circularProgressDrawable)
                    .into(holder.itemView.iv_image)
                tv_gender.text = member.detail.gender.capitalize()
                tv_age.text = (c as CommunityActivity).getAgeFromDOB(
                    member.detail.date_of_birth.split("-")[0].toInt(),
                    member.detail.date_of_birth.split("-")[1].toInt(),
                    member.detail.date_of_birth.split("-")[2].toInt()
                )
            }
            tv_user.text = members[position].first_name + " " + members[position].last_name

            RxView.clicks(cont_child).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onCommunityItemClick(member)
            }

            RxView.clicks(tv_add).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onCommunityItemAddClick(position)
            }

            RxView.clicks(tv_chat).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onCommunityItemChatClick(position, member)
            }
        }

        when (mode) {
            "viewmembers" -> {
                holder.itemView.btns.visibility = View.GONE
            }
            "editmembers" -> {
                holder.itemView.tv_add.text = "Remove"
                holder.itemView.tv_chat.visibility = View.GONE
            }
            "chat" -> {
                holder.itemView.tv_add.visibility = View.GONE
                holder.itemView.tv_chat.visibility = View.VISIBLE
            }
        }

    }

    override fun getItemCount(): Int = members.size

    interface CommunityAdapterClickListener {
        fun onCommunityItemClick(user: GetUsersData)
        fun onCommunityItemAddClick(position: Int)
        fun onCommunityItemChatClick(position: Int, member: GetUsersData)
    }
}