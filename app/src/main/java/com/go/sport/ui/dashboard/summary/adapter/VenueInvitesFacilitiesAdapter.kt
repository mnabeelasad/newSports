package com.go.sport.ui.dashboard.summary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.model.invites.InvitesFeature
import kotlinx.android.synthetic.main.row_ground_amenities.view.*

class VenueInvitesFacilitiesAdapter(
    private val c: Context,
    private val amenities: ArrayList<InvitesFeature>
) : RecyclerView.Adapter<VenueInvitesFacilitiesAdapter.VenueFacilitiesAdapterViewHolder>() {
    class VenueFacilitiesAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VenueFacilitiesAdapterViewHolder {
        return VenueFacilitiesAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_ground_amenities, parent, false)
        )
    }

    override fun getItemCount(): Int = amenities.size

    override fun onBindViewHolder(holder: VenueFacilitiesAdapterViewHolder, position: Int) {
        val amenity = amenities[position]

        holder.itemView.apply {
            tv_amenities.text = amenity.name
            Glide.with(c).load(amenity.image).into(iv_amenities)
        }
    }
}