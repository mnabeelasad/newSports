package com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.selectsports.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.extensions.setTint
import com.go.sport.model.getsports.GetSportsSport
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_select_sports.view.*
import java.util.concurrent.TimeUnit

/**
 * Created by muneeb on 08,September,2020
 */
class SelectSportsAdapter(
    val context: Context,
    private val items: ArrayList<GetSportsSport>,
    private val listener: OnSelectSportsClickListener
) :
        RecyclerView.Adapter<SelectSportsAdapter.SelectSportsAdapterViewHolder>() {
    class SelectSportsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): SelectSportsAdapterViewHolder {
        return SelectSportsAdapterViewHolder(
                LayoutInflater.from(context).inflate(R.layout.row_select_sports, parent, false)
        )
    }

    override fun getItemCount(): Int = items.size

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: SelectSportsAdapterViewHolder, position: Int) {
        val selectSport = items[position]

        holder.itemView.apply {
            if (selectSport.isSelected) {
                iv_sports.background = context.getDrawable(R.drawable.ic_back_sports)
                iv_sports.setTint(Color.WHITE)
            }
                //iv_tick.visibility = View.VISIBLE
            else {
                iv_sports.background = context.getDrawable(R.drawable.ic_sports_back)
                iv_sports.setTint(context.getColor(R.color.purple_dark) )
            }
            iv_tick.visibility = View.GONE

            Glide.with(context).load(selectSport.image).into(iv_sports)
            tv_sports.text = selectSport.name

            RxView.clicks(this).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                listener.onSelectSportsItemClick(position, selectSport)
            }
        }
    }

    interface OnSelectSportsClickListener {
        fun onSelectSportsItemClick(position: Int, selectSport: GetSportsSport)
    }
}