package com.go.sport.ui.dashboard.fragments.book.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.model.getfacilities.GetFacilitiesFeatures
import kotlinx.android.synthetic.main.row_facility_sports.view.*

class FeatureAdapter(
    private val c: Context,
    private val features: ArrayList<GetFacilitiesFeatures>
) :
    RecyclerView.Adapter<FeatureAdapter.BookAdapterViewHolder>() {
    class BookAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapterViewHolder {
        return BookAdapterViewHolder(
            LayoutInflater.from(c).inflate(
                R.layout.row_facility_sports,
                parent,
                false
            )
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: BookAdapterViewHolder, position: Int) {
        val facility = features[position]

        val circularProgressDrawable = CircularProgressDrawable(c)
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(c).load(facility.sport.image).placeholder(circularProgressDrawable)
            .into(holder.itemView.iv_facility_sport)
    }

    override fun getItemCount(): Int = features.size
}