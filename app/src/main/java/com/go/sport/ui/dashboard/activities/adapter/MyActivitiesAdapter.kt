package com.go.sport.ui.dashboard.activities.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R

class MyActivitiesAdapter(private val c: Context) :
    RecyclerView.Adapter<MyActivitiesAdapter.MyActivitiesAdapterViewHolder>() {
    class MyActivitiesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyActivitiesAdapterViewHolder {
        return MyActivitiesAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_invites, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyActivitiesAdapterViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 10
}
