package com.go.sport.ui.dashboard.fragments.wallet.transfermoney.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.model.getusers.GetUserSport
import com.go.sport.model.getusers.GetUsersData
import com.go.sport.ui.dashboard.fragments.home.adapter.UserSportsAdapter
import com.go.sport.ui.dashboard.fragments.wallet.transfermoney.TransferMoneyActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_search_user.view.*
import java.util.concurrent.TimeUnit

class SearchUserAdapter(
    private val c: Context,
    private val searchedUsers: ArrayList<GetUsersData>,
    private val l: SearchUserAdapterClickListener,
) :
    RecyclerView.Adapter<SearchUserAdapter.CategoriesAdapterViewHolder>() {
    class CategoriesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapterViewHolder {
        return CategoriesAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_search_user, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: CategoriesAdapterViewHolder, position: Int) {
        val searchedUser = searchedUsers[position]

        holder.itemView.apply {
            rv_sports.apply {
                layoutManager = LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false)
                adapter = UserSportsAdapter(c, searchedUser.sport as ArrayList<GetUserSport>)
            }

            simple_rating_bar.visibility = View.GONE
            tv_user.text = searchedUser.first_name + " " + searchedUser.last_name
            if (searchedUser.detail != null) {
                val circularProgressDrawable = CircularProgressDrawable(c)
                circularProgressDrawable.centerRadius = 15f
                circularProgressDrawable.start()

                Glide.with(c).load(searchedUser.detail.profile_image)
                    .placeholder(circularProgressDrawable).into(holder.itemView.iv_user)
                tv_gender.text = searchedUser.detail.gender.capitalize()
                tv_age.text = (c as TransferMoneyActivity).getAgeFromDOB(
                    searchedUser.detail.date_of_birth.split("-")[0].toInt(),
                    searchedUser.detail.date_of_birth.split("-")[1].toInt(),
                    searchedUser.detail.date_of_birth.split("-")[2].toInt(),
                )
            }

            if (searchedUser.isClicked) {
                cont_child.background =
                    ContextCompat.getDrawable(c, R.drawable.stroke_purple_10_radius)
            } else {
                cont_child.background =
                    ContextCompat.getDrawable(c, R.drawable.stroke_grey_10_radius)
            }

            RxView.clicks(this).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onSearchUserItemClick(searchedUser, position)
            }
        }
    }

    override fun getItemCount(): Int = searchedUsers.size

    interface SearchUserAdapterClickListener {
        fun onSearchUserItemClick(searchUser: GetUsersData, position: Int)
    }
}