package com.go.sport.ui.dashboard.fragments.book.hostactivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getslots.GetSlotsSlot
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_venue_time.view.*
import java.util.concurrent.TimeUnit

/**
 * Created by muneeb on 08,September,2020
 */
class TimeSlotsAdapter(
    val context: Context,
    private val items: ArrayList<GetSlotsSlot>,
    private val listener: OnTimeSlotsClickListener
) :
    RecyclerView.Adapter<TimeSlotsAdapter.ProductsAdapterViewHolder>() {
    class ProductsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsAdapterViewHolder {
        return ProductsAdapterViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_venue_time, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ProductsAdapterViewHolder, position: Int) {
        val slot = items[position]

        holder.itemView.apply {
            start_time.text = (context as BaseActivity).changeFormatTime("hh:mm:ss", slot.start, "hh:mm a")
          //  tv_duration.text = "Duration: " + slot.duration
            end_time.text = (context as BaseActivity).changeFormatTime("hh:mm:ss", slot.end, "hh:mm a")

            if (slot.status == 1)
                tv_slot_booking.text = "Un-Available"
            else
                tv_slot_booking.text = "Available"

            amount.text = "PKR " + slot.slot_price

            if (slot.isSelected) {
                cont_activity.background =
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.selected_slots,
                        null
                    )
            } else {
                cont_activity.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.stroke_white_time_slots, null)
            }

            RxView.clicks(this).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                listener.onTimeSlotsItemClick(position, slot)
            }

        }
    }

    interface OnTimeSlotsClickListener {
        fun onTimeSlotsItemClick(position: Int, slot: GetSlotsSlot)
    }
}