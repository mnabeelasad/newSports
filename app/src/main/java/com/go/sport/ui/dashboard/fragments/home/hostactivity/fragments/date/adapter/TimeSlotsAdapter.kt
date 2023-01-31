package com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.date.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getslots.GetSlotsSlot
import com.go.sport.singleton.Singleton
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_new_summary_screen.*
import kotlinx.android.synthetic.main.row_venue_time.view.*
import java.util.concurrent.TimeUnit

/**
 * Created by muneeb on 08,September,2020
 */
class TimeSlotsAdapter(
    val context: Context,
    private val slots: ArrayList<GetSlotsSlot>,
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

    override fun getItemCount(): Int = slots.size

    override fun onBindViewHolder(holder: ProductsAdapterViewHolder, position: Int) {
        val slot = slots[position]

        holder.itemView.apply {
            start_time.text = (context as BaseActivity).changeFormatTime("hh:mm:ss", slot.start, "hh:mm a")
            end_time.text = (context as BaseActivity).changeFormatTime("hh:mm:ss", slot.end, "hh:mm a")
            tv_duration.text = slot.duration + " Minutes"
            if (slot.status == 1){
                tv_slot_booking.text = "Un-Available"
            cont_activity.background =
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.selected_slots,
                    null
                )
            }
            else
                tv_slot_booking.text = "Available"

            amount.text = "PKR ${slot.slot_price}"

            if (slot.isSelected) {
                cont_activity.background =
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.selected_slots,
                        null
                    )
            } else {
                cont_activity.background =
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.back_slots,
                        null
                    )
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