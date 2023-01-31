package com.go.sport.ui.dashboard.chats.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.extensions.setGlide
import com.go.sport.model.firebase.ChatHeadsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_chats.view.*
import java.util.concurrent.TimeUnit

class ChatsAdapter(
    private val c: Context,
    val chatHeadsList: ArrayList<ChatHeadsModel>,
    private val l: ChatsClickListener
) :
    RecyclerView.Adapter<ChatsAdapter.ChatsAdapterViewHolder>() {
    class ChatsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatsAdapterViewHolder {
        return ChatsAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_chats, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatsAdapterViewHolder, position: Int) {
        val item = chatHeadsList[position]
        holder.itemView.apply {
            tv_name.text = item.name
            setOnClickListener {
                l.onChatsItemClick(position)
            }
            if (item.read == false) {
                main_row.setBackgroundResource(R.drawable.un_read_chats_bg)
            } else {
                main_row.setBackgroundResource(R.drawable.chats_read_bg)
            }

            iv_user.setGlide(item.image)
            tv_time.text = (c as BaseActivity).getDateTime(item.date)
            tv_date.text = c.getTime(item.date)
            if (item.group) {
                is_group.visibility = View.VISIBLE
            } else {
                is_group.visibility = View.GONE
            }

            tv_text.text = item.text
        }
    }

    override fun getItemCount(): Int = chatHeadsList.size

    interface ChatsClickListener {
        fun onChatsItemClick(position: Int)
    }
}
