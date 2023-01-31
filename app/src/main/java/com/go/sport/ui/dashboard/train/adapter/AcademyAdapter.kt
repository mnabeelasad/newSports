package com.go.sport.ui.dashboard.train.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.model.academies.AcademiesAcadmy
import kotlinx.android.synthetic.main.row_train_multi.view.*

class AcademyAdapter(
    private val c: Context,
    private val academies: ArrayList<AcademiesAcadmy>,
    private val l: AcademyAdapterClickListener
) :
    RecyclerView.Adapter<AcademyAdapter.AcademyAdapterVH>() {
    class AcademyAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcademyAdapterVH {
        return AcademyAdapterVH(
            LayoutInflater.from(c).inflate(R.layout.row_train_multi, parent, false)
        )
    }

    override fun getItemCount(): Int = academies.size

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: AcademyAdapterVH, position: Int) {
        val academy = academies[position]

        val circularProgressDrawable = CircularProgressDrawable(c)
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(c).load(academy.image).placeholder(circularProgressDrawable).into(holder.itemView.iv)
        holder.itemView.tv_title.text = academy.name
        holder.itemView.tv_desc.text = academy.description
        holder.itemView.tv_location.text = academy.address

        holder.itemView.setOnClickListener {
            l.onAcademyItemClick(academy)
        }
    }

    interface AcademyAdapterClickListener {
        fun onAcademyItemClick(academy: AcademiesAcadmy)
    }
}