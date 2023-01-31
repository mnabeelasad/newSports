package com.go.sport.ui.dashboard.fragments.home.mygroups.create.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.extensions.setGlide
import com.go.sport.model.getusers.GetUsersData
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_added_user.view.*
import java.util.concurrent.TimeUnit

class AddedUsersAdapter(
    private val c: Context,
    private val addedUsers: ArrayList<GetUsersData>,
    private val l: AddedUsersClickListener,
) :
    RecyclerView.Adapter<AddedUsersAdapter.UsersAdapterViewHolder>() {
    class UsersAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var rv: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rv = recyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersAdapterViewHolder {
        val view =
            UsersAdapterViewHolder(
                LayoutInflater.from(c).inflate(R.layout.row_added_user, parent, false)
            )

        view.itemView.layoutParams.width = (rv.measuredWidth / 4.8).toInt()

        return view
    }

    override fun onBindViewHolder(holder: UsersAdapterViewHolder, position: Int) {
        holder.itemView.apply {
//            RxView.clicks(iv_user).throttleFirst(2, TimeUnit.SECONDS).subscribe {
//                l.onAddedUsersItemClick(position)
//            }

            if(addedUsers[position].detail != null) {
                if (!addedUsers[position].detail.profile_image.isNullOrBlank()) {
                    iv_user.setGlide(addedUsers[position].detail.profile_image)
                }
                if (!addedUsers[position].first_name.isNullOrBlank() && !addedUsers[position].last_name.isNullOrBlank()){
                    tv_name.text = addedUsers[position].first_name.capitalize() + " " + addedUsers[position].last_name.capitalize()
                }
            }

            RxView.clicks(iv_cross).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onAddedUsersItemCrossClick(position)
            }
        }
    }

    override fun getItemCount(): Int = addedUsers.size

    interface AddedUsersClickListener {
//        fun onAddedUsersItemClick(position: Int)
        fun onAddedUsersItemCrossClick(position: Int)
    }
}
