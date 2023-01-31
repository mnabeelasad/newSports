package com.go.sport.ui.dashboard.fragments.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.model.HomeCategoriesModel
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_category.view.*
import java.util.concurrent.TimeUnit

class CategoriesAdapter(
    private val c: Context,
    private val l: CategoriesClickListener,
    private val categories: ArrayList<HomeCategoriesModel>
) :
    RecyclerView.Adapter<CategoriesAdapter.CategoriesAdapterViewHolder>() {
    class CategoriesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var rv: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rv = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapterViewHolder {
        val view = CategoriesAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_category, parent, false)
        )

//        view.itemView.layoutParams.width = (rv.measuredWidth / 5.5).toInt()

        return view
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: CategoriesAdapterViewHolder, position: Int) {
        val category = categories[position]
        holder.itemView.apply {
            iv_category.setImageResource(category.image)
            tv_category.text = category.title
        }

        RxView.clicks(holder.itemView).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            l.onCategoryItemClick(position)
        }
    }

    override fun getItemCount(): Int = categories.size

    interface CategoriesClickListener {
        fun onCategoryItemClick(position: Int)
    }
}