package com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.venue.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.model.getfeatures.GetFeaturesData
import kotlinx.android.synthetic.main.row_ground_amenities.view.*

class VenueFacilitiesAdapter(
    private val c: Context,
    private val amenities: ArrayList<GetFeaturesData>
) : RecyclerView.Adapter<VenueFacilitiesAdapter.VenueFacilitiesAdapterViewHolder>() {
    class VenueFacilitiesAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VenueFacilitiesAdapterViewHolder {
        return VenueFacilitiesAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_ground_amenities, parent, false)
        )
    }

    var onVenueClickListener : OnVenueClickListener? = null
    fun setListener(onVenueClickListener: OnVenueClickListener){
        this.onVenueClickListener = onVenueClickListener
    }

    override fun getItemCount(): Int = amenities.size

    override fun onBindViewHolder(holder: VenueFacilitiesAdapterViewHolder, position: Int) {
        val amenity = amenities[position]

        holder.itemView.apply {
            tv_amenities.text = amenity.name
            Glide.with(c).load(amenity.image).into(iv_amenities)
        }

        if(amenity.isSelected){
            holder.itemView.main.setBackgroundResource(R.drawable.bg_amenities_selected)
            ImageViewCompat.setImageTintList( holder.itemView.iv_amenities, ColorStateList.valueOf(
                ContextCompat.getColor(c, R.color.white)))
            holder.itemView.tv_amenities.setTextColor(
                ContextCompat.getColor(
                    c,
                    R.color.white
                ))
        }else{
            holder.itemView.main.setBackgroundResource(R.drawable.bg_amenities_unselected)
            ImageViewCompat.setImageTintList( holder.itemView.iv_amenities, ColorStateList.valueOf(
                ContextCompat.getColor(c, R.color.purple_dark_3)));
            holder.itemView.tv_amenities.setTextColor(
                ContextCompat.getColor(
                    c,
                    R.color.purple_dark_3
                ))
        }

        holder.itemView.setOnClickListener {
            onVenueClickListener?.onVenueclicked(position, amenity)
        }
    }
}

interface OnVenueClickListener{
    fun onVenueclicked(
        position: Int,
        feature: GetFeaturesData
    )
}