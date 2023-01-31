package com.go.sport.ui.dashboard.fragments.home.hostactivity.community.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.extensions.setGlide
import com.go.sport.model.unjoinedgroups.ListUnjoinedData
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_community_selectable.view.*
import kotlinx.android.synthetic.main.row_group.view.*
import kotlinx.android.synthetic.main.row_group.view.cb
import kotlinx.android.synthetic.main.row_group.view.iv_user
import kotlinx.android.synthetic.main.row_group.view.tv_user
import java.util.concurrent.TimeUnit

/**
 * Created by muneeb on 08,September,2020
 */
class GroupAdapter(
    val context: Context,
    private val items: ArrayList<ListUnjoinedData>,
    private val listener: OnGroupClickListener
) :
    RecyclerView.Adapter<GroupAdapter.GroupAdapterViewHolder>() {
    class GroupAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupAdapterViewHolder {
        return GroupAdapterViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_group, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: GroupAdapterViewHolder, position: Int) {
        val filter = items[position]

        holder.itemView.apply {
//                       Glide.with(context).load(filter.image).into(iv_user)
            tv_user.text = filter.name



            cb.isChecked = filter.isSelected

            RxView.clicks(this).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                listener.onGroupItemClick(position, filter)
            }
        }
    }

    interface OnGroupClickListener {
        fun onGroupItemClick(position: Int, filter: ListUnjoinedData)
    }
}