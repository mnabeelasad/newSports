package com.go.sport.ui.dashboard.fragments.home.mygroups.adapter

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
import com.go.sport.base.BaseActivity
import com.go.sport.model.getusers.GetUserSport
import com.go.sport.model.viewgroup.ViewGroupData
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.ui.dashboard.fragments.home.adapter.UserSportsAdapter
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_community.view.*
import java.util.concurrent.TimeUnit

class GroupMembersAdapter(
    private val c: Context,
    private val l: GroupMembersClickListener,
    val members: ArrayList<ViewGroupData>,
    val created_by: String
) :
    RecyclerView.Adapter<GroupMembersAdapter.GroupMembersAdapterViewHolder>() {
    class GroupMembersAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupMembersAdapterViewHolder {
        return GroupMembersAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_community, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: GroupMembersAdapterViewHolder, position: Int) {
        val member = members[position]

        holder.itemView.apply {

            rv_sports.apply {
                layoutManager = LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false)
                adapter =
                    UserSportsAdapter(c, members[position].user.sport as ArrayList<GetUserSport>)
            }


            val circularProgressDrawable = CircularProgressDrawable(c)
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            if (member.user.detail != null) {
                Glide.with(c).load(member.user.detail.profile_image)
                    .placeholder(circularProgressDrawable)
                    .into(holder.itemView.iv_image)
                tv_gender.text = member.user.detail.gender.capitalize()
                tv_age.text = (c as BaseActivity).getAgeFromDOB(
                    member.user.detail.date_of_birth.split("-")[0].toInt(),
                    member.user.detail.date_of_birth.split("-")[1].toInt(),
                    member.user.detail.date_of_birth.split("-")[2].toInt()
                )
            }
            tv_user.text =
                members[position].user.first_name + " " + members[position].user.last_name

            RxView.clicks(cont_child).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onGroupItemClick(position)
            }

            RxView.clicks(tv_add).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onGroupItemAddClick(position)
            }

            RxView.clicks(tv_chat).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onGroupItemChatClick(position)
            }
            if (created_by == MySharedPreference(c).getUserObject()?.id.toString()) {
                btns.visibility = View.VISIBLE
            } else {
                btns.visibility = View.GONE
            }
            if (created_by == members[position].member_id) {
                btns.visibility = View.GONE
            } else {
                btns.visibility = View.VISIBLE
            }


        }

        /*     when (mode) {
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
             }*/


        if (created_by == MySharedPreference(c).getUserObject()?.id.toString()) {
            holder.itemView.tv_add.text = "Remove"
            holder.itemView.btn_view.visibility = View.GONE
            holder.itemView.tv_chat.visibility = View.GONE
        }
        else if (created_by != MySharedPreference(c).getUserObject()?.id.toString()) {

            if(member.member_id == MySharedPreference(c).getUserObject()?.id.toString()){
//            while (members[position].user.detail.user_id == MySharedPreference(c).getUserObject()?.detail?.user_id) {
                holder.itemView.tv_add.text = "Leave"
                holder.itemView.btn_view.visibility = View.GONE
                holder.itemView.tv_chat.visibility = View.GONE
            }
            else {
                holder.itemView.btns.visibility = View.GONE

            }
        }

    }

    override fun getItemCount(): Int = members.size

    interface GroupMembersClickListener {
        fun onGroupItemClick(position: Int)
        fun onGroupItemAddClick(position: Int)
        fun onGroupItemChatClick(position: Int)
    }
}