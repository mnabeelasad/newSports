package com.go.sport.ui.dashboard.chats

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.firebase.ChatHeadsModel
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.ui.dashboard.chats.adapter.ChatsAdapter
import com.go.sport.ui.dashboard.fragments.home.community.CommunityActivity
import com.go.sport.ui.dashboard.fragments.home.mygroups.MyGroupsActivity
import com.google.firebase.database.*
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_chats.*
import java.util.concurrent.TimeUnit

class ChatsActivity : BaseActivity(), ChatsAdapter.ChatsClickListener {

    private var isFabClicked = false
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        initViews()
        initListeners()
        getChatHead()
    }

    var chatHeadsList: ArrayList<ChatHeadsModel> = ArrayList()
    private fun getChatHead() {
        val userId = MySharedPreference(this).getUserObject()?.id.toString() ?: ""

        database = FirebaseDatabase.getInstance().reference
            .child("Conversation")
            .child(userId)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //   pBar(0, this@TraineeChatListingActivity)
                chatHeadsList.clear()
                dataSnapshot.children.mapNotNullTo(chatHeadsList) {
                    it.getValue(ChatHeadsModel::class.java)
                }
                chatHeadsList.sortByDescending { it.date }

                rv_chats.adapter!!.notifyDataSetChanged()
                if (chatHeadsList.size > 0) {
                    tv_no_chats.visibility = View.GONE
                } else {
                    tv_no_chats.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //    pBar(0, this@TraineeChatListingActivity)
                Log.w("chatheads", "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(postListener)
    }

    private fun initViews() {
        rv_chats.apply {
            layoutManager = LinearLayoutManager(this@ChatsActivity)
            adapter = ChatsAdapter(this@ChatsActivity, chatHeadsList, this@ChatsActivity)
        }
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }
        //GROUPS
        RxView.clicks(fab3).subscribe {
            var bundle = Bundle()
            bundle.putString("mode", "chat")
            startActivity(this, MyGroupsActivity::class.java, false, 1, bundle)
        }
        //USERS
        RxView.clicks(fab2).subscribe {
            var bundle = Bundle()
            bundle.putString("mode", "chat")
            startActivity(this, CommunityActivity::class.java, false, 1, bundle)
        }

        RxView.clicks(fab).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            if (isFabClicked) {
                isFabClicked = false
                fab2.visibility = View.GONE
                fab2.animate().rotation(0f).start()
                fab3.visibility = View.GONE
                fab3.animate().rotation(0f).start()
            } else {
                isFabClicked = true
                fab2.visibility = View.VISIBLE
                fab2.animate().rotation(360f).start()
                fab3.visibility = View.VISIBLE
                fab3.animate().rotation(360f).start()
            }
        }
    }


    override fun onChatsItemClick(position: Int) {
        if (chatHeadsList[position].blocked == "false") {

            FirebaseDatabase.getInstance().reference
                .child("Conversation")
                .child(chatHeadsList[position].id)
                .child(MySharedPreference(this).getUserObject()?.id.toString() ?: "")
                .child("read")
                .setValue(
                    true
                )

            val bundle = Bundle()
            bundle.putParcelable("userObject", chatHeadsList[position])
            startActivity(this, ConversationActivity::class.java, false, 1, bundle)
        } else
            warningToast("You can't send message to this user")
    }
}