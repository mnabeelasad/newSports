package com.go.sport.ui.dashboard.chats.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.extensions.setGlide
import com.go.sport.model.firebase.ChatHeadsModel
import com.go.sport.model.firebase.chatmodel.ChatModel
import com.go.sport.sharedpref.MySharedPreference
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_chat_view.view.*
import java.util.concurrent.TimeUnit

class ConversationAdapter(
    private val c: Context,
    private val l: ConversationClickListener,
    val chatHead: ChatHeadsModel,
    val chatList: ArrayList<ChatModel>,
    val userId: String
) :
    RecyclerView.Adapter<ConversationAdapter.ChatsAdapterViewHolder>() {
    class ChatsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatsAdapterViewHolder {
        return ChatsAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_chat_view, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ChatsAdapterViewHolder, position: Int) {
        RxView.clicks(holder.itemView).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            l.onConversationItemClick(position)
        }

        val item = chatList[position]
        holder.itemView.apply {
            if(item.sender.id == userId){
                receiver_cont.visibility = View.GONE
                sender_cont.visibility = View.VISIBLE

                tv_sender_time.text = (c as BaseActivity).getTime(item.date)
                tv_sender_date.text = c.getDateTime(item.date)
                tv_sender_msg.text = item.data.text


            }else{
                receiver_cont.visibility = View.VISIBLE
                sender_cont.visibility = View.GONE

                receiver_dp.setGlide(chatHead.image)
                tv_receiver_message.text = item.data.text
                tv_receiver_time.text = (c as BaseActivity).getTime(item.date)
                tv_receiver_date.text = c.getDateTime(item.date)
            }
        }
    }

    override fun getItemCount(): Int = chatList.size

    interface ConversationClickListener {
        fun onConversationItemClick(position: Int)
    }
}
