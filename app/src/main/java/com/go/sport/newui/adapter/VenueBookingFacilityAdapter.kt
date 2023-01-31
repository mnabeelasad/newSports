package com.go.sport.newui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.go.sport.R

import com.go.sport.model.getfeatures.GetFeaturesData

import kotlinx.android.synthetic.main.row_ground_amenities.view.*


class VenueBookingFacilityAdapter(
    private val c: Context,
    private val facilities: ArrayList<GetFeaturesData>
) : RecyclerView.Adapter<VenueBookingFacilityAdapter.VenueBookingFacilityAdapterViewHolder>() {
    class VenueBookingFacilityAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VenueBookingFacilityAdapterViewHolder {
        return VenueBookingFacilityAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.amaneties_adapter, parent, false)
        )
    }

    override fun getItemCount(): Int = facilities.size

    override fun onBindViewHolder(holder: VenueBookingFacilityAdapterViewHolder, position: Int) {
        val amenity = facilities[position]

        holder.itemView.apply {
            tv_amenities.text = amenity.name
            Glide.with(c).load(amenity.image).into(iv_amenities)
        }
    }
}