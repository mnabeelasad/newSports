package com.go.sport.ui.dashboard.fragments.home.hostactivity.community.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.extensions.setGlide
import com.go.sport.base.BaseActivity
import com.go.sport.model.getusers.GetUsersData
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_community_selectable.view.*
import java.util.concurrent.TimeUnit

/**
 * Created by muneeb on 08,September,2020
 */
class CommunityAdapter(
    val context: Context,
    private val items: ArrayList<GetUsersData>,
    private val listener: OnCommunityClickListener
) :
    RecyclerView.Adapter<CommunityAdapter.CommunityAdapterViewHolder>() {
    class CommunityAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityAdapterViewHolder {
        return CommunityAdapterViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_community_selectable, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CommunityAdapterViewHolder, position: Int) {
        val filter = items[position]

        holder.itemView.apply {
            if (filter.detail != null) {
                Glide.with(context).load(filter.detail.profile_image).into(iv_user)
                tv_gender.text = filter.detail.gender.capitalize()
                tv_age.text = (context as BaseActivity).getAgeFromDOB(
                    filter.detail.date_of_birth.split("-")[0].toInt(),
                    filter.detail.date_of_birth.split("-")[1].toInt(),
                    filter.detail.date_of_birth.split("-")[2].toInt()
                ) + " Years"
            }
            tv_user.text = "${filter.first_name} ${filter.last_name}"

            cb.isChecked = filter.isClicked

            RxView.clicks(this).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                listener.onCommunityItemClick(position, filter)
            }
        }
    }

    interface OnCommunityClickListener {
        fun onCommunityItemClick(position: Int, filter: GetUsersData)
    }
}