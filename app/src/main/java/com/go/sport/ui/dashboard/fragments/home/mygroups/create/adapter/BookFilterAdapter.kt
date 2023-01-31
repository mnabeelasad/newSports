package com.go.sport.ui.dashboard.fragments.home.mygroups.create.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.model.getsports.GetSportsSport
import com.go.sport.ui.dashboard.activities.FilterModel
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_bottom_sheet_filter.view.*
import java.util.concurrent.TimeUnit

/**
 * Created by muneeb on 08,September,2020
 */
class BookFilterAdapter(
    val context: Context,
    private val items: ArrayList<FilterModel>,
    private val filterIds: ArrayList<Int>,
    private val listener: OnFiltersClickListener
) :
    RecyclerView.Adapter<BookFilterAdapter.ProductsAdapterViewHolder>() {
    class ProductsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsAdapterViewHolder {
        return ProductsAdapterViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_bottom_sheet_filter, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ProductsAdapterViewHolder, position: Int) {
        val filter = items[position]

        holder.itemView.apply {
            tv_filter.text = filter.title

            if (filterIds.contains(filter.id.toInt())) {
                cont_filter.background =
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.bg_activity_status,
                        null
                    )
                tv_filter.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                cont_filter.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.stroke_grey_5_radius, null)
                tv_filter.setTextColor(ContextCompat.getColor(context, R.color.grey_3))
            }

            RxView.clicks(this).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                listener.onFilterItemClick(position, filter)
            }
        }
    }

    interface OnFiltersClickListener {
        fun onFilterItemClick(position: Int, filter: FilterModel)
    }
}