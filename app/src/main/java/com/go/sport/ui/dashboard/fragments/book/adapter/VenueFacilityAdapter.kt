package com.go.sport.ui.dashboard.fragments.book.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R

class VenueFacilityAdapter (
    private val c: Context,
//    private val amenities: ArrayList<GetFacilityDataFeature>
) : RecyclerView.Adapter<VenueFacilityAdapter.VenueFacilityAdapterViewHolder>() {
    class VenueFacilityAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VenueFacilityAdapterViewHolder {
        return VenueFacilityAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.venue_timing_recycler, parent, false)
        )
    }


    override fun onBindViewHolder(holder: VenueFacilityAdapterViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 7
}