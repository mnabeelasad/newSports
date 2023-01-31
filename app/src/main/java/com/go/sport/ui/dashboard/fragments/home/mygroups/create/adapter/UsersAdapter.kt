package com.go.sport.ui.dashboard.fragments.home.mygroups.create.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.extensions.setGlide
import com.go.sport.model.getusers.GetUsersData
import com.go.sport.ui.dashboard.DashboardActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_community.view.*
import java.util.concurrent.TimeUnit

class UsersAdapter(
    private val c: Context,
    private val users: ArrayList<GetUsersData>,
    private val l: UsersAdapterClickListener,
) :
    RecyclerView.Adapter<UsersAdapter.UsersAdapterViewHolder>() {
    class UsersAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapterViewHolder {
        return UsersAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_community, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: UsersAdapterViewHolder, position: Int) {
        val user = users[position]
        holder.itemView.apply {

            if (user.detail != null) {
                if (!user.detail.profile_image.isNullOrBlank())
                    iv_image.setGlide(user.detail.profile_image)
                tv_user.text = user.first_name + " " + user.last_name
                tv_age.text = (context as BaseActivity).getAgeFromDOB(
                    user.detail.date_of_birth.split("-")[0].toInt(),
                    user.detail.date_of_birth.split("-")[1].toInt(),
                    user.detail.date_of_birth.split("-")[2].toInt()
                )

                if (user.isClicked)
                    tv_add.text = "Added"
                else
                    tv_add.text = "Add"

                RxView.clicks(cont_child).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                    l.onUsersItemClick(position)
                }

                RxView.clicks(tv_add).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                    l.onUsersItemAddClick(position)
                }

                RxView.clicks(tv_chat).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                    l.onUsersItemChatClick(position)
                }
            }
        }
    }

    override fun getItemCount(): Int = users.size

    interface UsersAdapterClickListener {
        fun onUsersItemClick(position: Int)
        fun onUsersItemAddClick(position: Int)
        fun onUsersItemChatClick(position: Int)
    }
}