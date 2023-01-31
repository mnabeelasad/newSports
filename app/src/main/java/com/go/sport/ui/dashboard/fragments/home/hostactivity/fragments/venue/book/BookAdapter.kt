package com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.venue.book

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.model.getfacilities.GetFacilitiesData
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_book.view.*
import java.util.concurrent.TimeUnit

class BookAdapter(
    private val c: Context,
    private val facilities: ArrayList<GetFacilitiesData>,
    private val l: BookClickListener
) :
    RecyclerView.Adapter<BookAdapter.BookAdapterViewHolder>() {
    class BookAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapterViewHolder {
        return BookAdapterViewHolder(
            LayoutInflater.from(c).inflate(
                R.layout.row_book,
                parent,
                false
            )
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: BookAdapterViewHolder, position: Int) {
        val facility = facilities[position]

        val circularProgressDrawable = CircularProgressDrawable(c)
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(c).load(facility.image).placeholder(circularProgressDrawable).into(holder.itemView.iv)
        holder.itemView.tv_title.text = facility.name
        holder.itemView.tv_location.text = facility.address
        holder.itemView.tv_phone.text = facility.phone
        holder.itemView.tv_pitch_size.text = facility.pitchsize
        holder.itemView.tv_price.text = facility.pricing
        holder.itemView.tv_rating.text = facility.rating
        holder.itemView.simple_rating_bar.rating = facility.rating.toFloat()

        RxView.clicks(holder.itemView.tv_book_now).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            l.onBookItemClick(position)
        }
    }

    override fun getItemCount(): Int = facilities.size

    interface BookClickListener {
        fun onBookItemClick(position: Int)
    }
}