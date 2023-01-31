package com.go.sport.ui.dashboard.venueAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.model.getfeatures.GetFeaturesData
import kotlinx.android.synthetic.main.row_ground_amenities.view.*

class AmenetiesAdapter(
    private val c: Context,
    private val amenities: ArrayList<GetFeaturesData>
) : RecyclerView.Adapter<AmenetiesAdapter.AmenetiesAdapterViewHolder>() {
    class AmenetiesAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AmenetiesAdapterViewHolder {
        return AmenetiesAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.amaneties_adapter, parent, false)
        )
    }

    override fun getItemCount(): Int = amenities.size

    override fun onBindViewHolder(holder: AmenetiesAdapterViewHolder, position: Int) {
        val amenity = amenities[position]

        holder.itemView.apply {
            tv_amenities.text = amenity.name
            Glide.with(c).load(amenity.image).into(iv_amenities)
        }
    }
}