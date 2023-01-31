package com.go.sport.ui.dashboard.fragments.home.mygroups.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_users.view.*
import java.util.concurrent.TimeUnit

class UsersAdapter(private val c: Context, private val l: UsersClickListener) :
    RecyclerView.Adapter<UsersAdapter.UsersAdapterViewHolder>() {
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
                LayoutInflater.from(c).inflate(R.layout.row_users, parent, false)
            )

        view.itemView.layoutParams.width = (rv.measuredWidth / 4.8).toInt()

        return view
    }

    override fun onBindViewHolder(holder: UsersAdapterViewHolder, position: Int) {
        holder.itemView.apply {
            RxView.clicks(iv_user).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onUsersItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int = 10

    interface UsersClickListener {
        fun onUsersItemClick(position: Int)
    }
}
