package com.go.sport.ui.dashboard.train.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCheckedTextView
import kotlinx.android.synthetic.main.row_train.view.*
import java.util.concurrent.TimeUnit

class IndividualAdapter(private val c: Context, private val l: IndividualAdapterClickListener) :
    RecyclerView.Adapter<IndividualAdapter.IndividualAdapterVH>() {
    class IndividualAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndividualAdapterVH {
        return IndividualAdapterVH(
            LayoutInflater.from(c).inflate(R.layout.row_train_multi, parent, false)
        )
    }

    override fun getItemCount(): Int = 10

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: IndividualAdapterVH, position: Int) {
        holder.itemView.setOnClickListener {
            l.onIndividualClick(position)
        }

    }

    interface IndividualAdapterClickListener {
        fun onIndividualClick(position: Int)
    }
}