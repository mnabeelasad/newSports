package com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.date.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.model.getfacilitydata.GetFacilityDataPitchsize
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_available_pitches.view.*
import java.util.concurrent.TimeUnit

/**
 * Created by muneeb on 08,September,2020
 */
class AvailablePitchesAdapter(
    val context: Context,
    private val items: ArrayList<GetFacilityDataPitchsize>,
    private val listener: OnAvailablePitchesClickListener
) :
    RecyclerView.Adapter<AvailablePitchesAdapter.ProductsAdapterViewHolder>() {
    class ProductsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsAdapterViewHolder {
        return ProductsAdapterViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_available_pitches, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ProductsAdapterViewHolder, position: Int) {
        val filter = items[position]

        holder.itemView.apply {
            tv_pitch.text = filter.name

            if (filter.isSelected) {
                cont_pitch.background =
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.bg_activity_status,
                        null
                    )
                tv_pitch.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                cont_pitch.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.stroke_purple_5_radius, null)
                tv_pitch.setTextColor(ContextCompat.getColor(context, R.color.purple_dark_3))
            }

            RxView.clicks(this).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                listener.onAvailablePitchesItemClick(position, filter)
            }
        }
    }

    interface OnAvailablePitchesClickListener {
        fun onAvailablePitchesItemClick(position: Int, pitch: GetFacilityDataPitchsize)
    }
}