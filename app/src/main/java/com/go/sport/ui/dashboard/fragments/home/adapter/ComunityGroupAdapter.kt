package com.go.sport.ui.dashboard.fragments.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.model.HomeCategoriesModel
import com.go.sport.model.unjoinedgroups.ListUnjoinedData
import com.go.sport.ui.dashboard.fragments.home.mygroups.adapter.MyGroupsAdapter
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_category.view.*
import kotlinx.android.synthetic.main.row_group.view.*
import kotlinx.android.synthetic.main.row_my_groups.view.*
import kotlinx.android.synthetic.main.row_my_groups.view.tv_user
import java.util.concurrent.TimeUnit

class ComunityGroupAdapter(
    private val c: Context,
    val myGroups: ArrayList<ListUnjoinedData>,
    private val l: onItemClickListener
) :
    RecyclerView.Adapter<ComunityGroupAdapter.ComunityGroupAdapterViewHolder>() {
    class ComunityGroupAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var rv: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rv = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComunityGroupAdapterViewHolder {
        val view = ComunityGroupAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_group_community, parent, false)
        )

//        view.itemView.layoutParams.width = (rv.measuredWidth / 5.5).toInt()

        return view
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ComunityGroupAdapterViewHolder, position: Int) {
        val item = myGroups[position]
        holder.itemView.apply {
            tv_user.text = item.name
            cb.visibility = View.GONE
        }

        RxView.clicks(holder.itemView).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            l.onItemSelected(position)
        }
    }

    override fun getItemCount(): Int = myGroups.size

 interface onItemClickListener{
     fun onItemSelected(pos : Int)
 }
}