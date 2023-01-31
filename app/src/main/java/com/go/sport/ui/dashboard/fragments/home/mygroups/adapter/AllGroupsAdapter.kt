package com.go.sport.ui.dashboard.fragments.home.mygroups.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.model.publicgroups.PublicGroupData
import com.go.sport.model.unjoinedgroups.ListUnjoinedData
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_all_groups.view.*
import java.util.concurrent.TimeUnit

class AllGroupsAdapter(
    private val c: Context,
    val allGroups: ArrayList<PublicGroupData>,
    private val l: AllGroupsClickListener
) :
    RecyclerView.Adapter<AllGroupsAdapter.AllGroupsAdapterViewHolder>() {
    class AllGroupsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var rv: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rv = recyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllGroupsAdapterViewHolder {
        val view =
            AllGroupsAdapterViewHolder(
                LayoutInflater.from(c).inflate(R.layout.row_all_groups, parent, false)
            )

//        view.itemView.layoutParams.width = (rv.measuredWidth / 1.1).toInt()

        return view
    }

    override fun onBindViewHolder(holder: AllGroupsAdapterViewHolder, position: Int) {

        val item = allGroups[position]
        holder.itemView.apply {
            tv_title.text = item.name
            tv_count.text = item.members.toString()
            tv_user.text = item.owner_name

        }

        /*  holder.itemView.tv_count.text = (position + 2).toString()
          *//*   if (position % 2 == 0) {
               holder.itemView.tv_join_group.text = "View Members"


           }
   */

        holder.itemView.apply {

            RxView.clicks(tv_join_group).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onAllGroupsViewMemberClick(position)
            }
            RxView.clicks(tv_chat).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onAllGroupsJoinGameClick(position)
            }
        }

    }

    override fun getItemCount(): Int = allGroups.size

    interface AllGroupsClickListener {
        fun onAllGroupsJoinGameClick(position: Int)
        fun onAllGroupsViewMemberClick(position: Int)
    }
}
