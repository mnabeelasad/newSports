package com.go.sport.ui.dashboard.fragments.home.mygroups.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.model.unjoinedgroups.ListUnjoinedData
import com.go.sport.sharedpref.MySharedPreference
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_my_groups.view.*
import java.util.concurrent.TimeUnit

class MyGroupsAdapter(
    private val c: Context,
    val myGroups: ArrayList<ListUnjoinedData>,
    private val l: MyGroupsClickListener
) :
    RecyclerView.Adapter<MyGroupsAdapter.MyGroupsAdapterViewHolder>() {
    class MyGroupsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var rv: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rv = recyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyGroupsAdapterViewHolder {
        val view =
            MyGroupsAdapterViewHolder(
                LayoutInflater.from(c).inflate(R.layout.row_my_groups, parent, false)
            )

//        view.itemView.layoutParams.width = (rv.measuredWidth / 1.1).toInt()

        return view
    }

    override fun onBindViewHolder(holder: MyGroupsAdapterViewHolder, position: Int) {

        val item = myGroups[position]
        holder.itemView.apply {
            tv_title.text = item.name
            tv_count.text = item.members.toString()
            tv_user.text = item.owner_name



            if (item.created_by == MySharedPreference(c).getUserObject()?.id.toString()) {
                tv_view_members.text = "View or Edit Members"
                ic_trash.visibility = View.VISIBLE

            } else {
                tv_view_members.text = "View Members"
                ic_trash.visibility = View.GONE
            }


            RxView.clicks(cont_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onMyGroupsItemClick(position)
            }

            RxView.clicks(tv_view_members).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onMyGroupsViewMebers(position)
            }
            RxView.clicks(tv_chat).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onMyGroupsChatClick(position)
            }

            RxView.clicks(ic_trash).throttleFirst(2,TimeUnit.SECONDS).subscribe {
                l.onGroupDelete(position)
            }

        }

        /* holder.itemView.tv_count.text = (position+2).toString()
         if (position % 2 == 0) {
             holder.itemView.tv_join_group.text = "View Members"


         }
 */


    }

    override fun getItemCount(): Int = myGroups.size

    interface MyGroupsClickListener {
        fun onMyGroupsItemClick(position: Int)
        fun onMyGroupsViewMebers(position: Int)
        fun onMyGroupsChatClick(position: Int)
        fun onGroupDelete(position: Int)
    }
}
