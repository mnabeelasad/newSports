package com.go.sport.ui.dashboard.playerprofile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.model.userprofile.GetUserProfileSport
import kotlinx.android.synthetic.main.row_filter.view.*

/**
 * Created by muneeb on 08,September,2020
 */
class InterestAdapter(
    val context: Context,
    private val interests: ArrayList<GetUserProfileSport>,
) :
    RecyclerView.Adapter<InterestAdapter.ProductsAdapterViewHolder>() {
    class ProductsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsAdapterViewHolder {
        return ProductsAdapterViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_filter, parent, false)
        )
    }

    override fun getItemCount(): Int = interests.size

    override fun onBindViewHolder(holder: ProductsAdapterViewHolder, position: Int) {
        val interest = interests[position]
        holder.itemView.tv_filter.text = interest.name
    }
}