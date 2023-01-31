package com.go.sport.ui.dashboard.fragments.home.mygroups

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdacciaro.iOSDialog.iOSDialogBuilder
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.creategroup.firebase.AddMemberFirebase
import com.go.sport.model.firebase.ChatHeadsModel
import com.go.sport.model.publicgroups.PublicGroupData
import com.go.sport.model.unjoinedgroups.ListUnjoinedData
import com.go.sport.network.APIManager
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.ui.dashboard.chats.ConversationActivity
import com.go.sport.ui.dashboard.fragments.home.mygroups.adapter.AllGroupsAdapter
import com.go.sport.ui.dashboard.fragments.home.mygroups.adapter.MyGroupsAdapter
import com.go.sport.ui.dashboard.fragments.home.mygroups.adapter.UsersAdapter
import com.go.sport.ui.dashboard.fragments.home.mygroups.create.CreateGroupActivity
import com.go.sport.ui.dashboard.playerprofile.PlayerProfileActivity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_invites.*
import kotlinx.android.synthetic.main.activity_my_groups.*
import kotlinx.android.synthetic.main.activity_my_groups.iv_back
import java.util.concurrent.TimeUnit

class MyGroupsActivity : BaseActivity(),
    UsersAdapter.UsersClickListener,
    MyGroupsAdapter.MyGroupsClickListener, AllGroupsAdapter.AllGroupsClickListener {

    private var myGroups = ArrayList<ListUnjoinedData>()
    private var allGroups = ArrayList<PublicGroupData>()
    var mode = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_groups)

        refresh()
        refreshPGroups()
        initViews()
        initListeners()
        initTabs()


        if (intent.hasExtra("mode")) {
            mode = intent.getStringExtra("mode").toString()
            if (mode == "chat") {
                tab_layout.visibility = View.GONE
                tv_title.text = "Select Group"
            }
        }
    }

    private fun refresh() {
        swipeRefreshLayout.setOnRefreshListener {
            getMyGroups(true)
            swipeRefreshLayout.setRefreshing(false)
        }
    }

    private fun refreshPGroups() {
        swipeRefresh.setOnRefreshListener {
            getPublicGroups(true)
            swipeRefresh.setRefreshing(false)
        }
    }

    override fun onResume() {
        super.onResume()

        getPublicGroups()
        getMyGroups()
    }

    private fun initTabs() {
        tab_layout.addTab(tab_layout.newTab().setText("My Groups"))
        tab_layout.addTab(tab_layout.newTab().setText("All Groups"))
    }

    private fun initViews() {
        rv_users.apply {
            layoutManager =
                LinearLayoutManager(this@MyGroupsActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = UsersAdapter(this@MyGroupsActivity, this@MyGroupsActivity)
        }

        rv_groups.adapter = MyGroupsAdapter(this, myGroups, this)
        rv_all_groups.adapter = AllGroupsAdapter(this, allGroups, this)
    }

    fun getPublicGroups(isRefresh: Boolean = false) {
        pBar(1)
        APIManager.publicGroups(returnUserAuthToken()) { result, error ->
            pBar(0)
            if (isRefresh)
                if (error != null) {
                    mOnError(error)
                    return@publicGroups
                }

            if (result?.status == true) {
                allGroups.clear()
                allGroups.addAll(result.data)
                rv_all_groups.adapter?.notifyDataSetChanged()
            }
        }
    }

    fun getMyGroups(isRefresh: Boolean = false) {
        pBar(1)
        APIManager.listJoinedGroups(returnUserAuthToken()) { result, error ->
            pBar(0)
            if (isRefresh)
                if (error != null) {
                    mOnError(error)
                    return@listJoinedGroups
                }

            if (result?.status == true) {
                myGroups.clear()
                myGroups.addAll(result.data)
                rv_groups.adapter?.notifyDataSetChanged()
            }
        }
    }


    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(cont_create_group).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivity(this, CreateGroupActivity::class.java, false, 1)
        }

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position ?: 0 == 0) {
                    rv_all_groups.visibility = View.GONE
                    rv_groups.visibility = View.VISIBLE
                } else {
                    rv_all_groups.visibility = View.VISIBLE
                    rv_groups.visibility = View.GONE
                }
            }
        })
    }

    override fun onUsersItemClick(position: Int) {
        startActivity(this, PlayerProfileActivity::class.java, false, 1)
    }

    override fun onMyGroupsItemClick(position: Int) {

    }

    override fun onMyGroupsViewMebers(position: Int) {

        var bundle = Bundle()

        bundle.putString("group_id", myGroups[position].id.toString())
        bundle.putString("created_by", myGroups[position].created_by)

        startActivity(this, GroupViewMembersActivity::class.java, false, 1, bundle)
    }

    override fun onMyGroupsChatClick(position: Int) {

        val bundle = Bundle()
        bundle.putParcelable(
            "userObject",
            ChatHeadsModel(
                myGroups[position].id.toString(),
                System.currentTimeMillis().toString(),
                "",
                true,
                myGroups[position].name,
                ""
            )
        )
        startActivity(this, ConversationActivity::class.java, false, 1, bundle)
    }

    override fun onGroupDelete(position: Int) {
        var dialog = iOSDialogBuilder(this)
            .setTitle("Go Play")
            .setSubtitle("Are you sure you want to Delete Group?")
            .setBoldPositiveLabel(true)
            .setCancelable(false)
            .setPositiveListener(
                "Ok"
            ) { dialog ->
                dialog.dismiss()
                groupDelete(position)
            }
            .setNegativeListener(
                "Cancel"
            ) { dialog -> dialog.dismiss() }
            .build().show()
    }

    override fun onAllGroupsJoinGameClick(position: Int) {

        addGroupMember(
            allGroups[position].id.toString(),
            MySharedPreference(this).getUserObject()!!.id.toString(),
            allGroups[position].name
        )
    }

    override fun onAllGroupsViewMemberClick(position: Int) {

        var bundle = Bundle()

        bundle.putString("group_id", allGroups[position].id.toString())
        bundle.putString("created_by", allGroups[position].created_by)

        startActivity(this, GroupViewMembersActivity::class.java, false, 1, bundle)
    }

    fun addGroupMember(group_id: String, memberId: String, name: String) {

        pBar(1)
        APIManager.addGroupMember(returnUserAuthToken(), group_id, memberId) { result, error ->
            pBar(0)



            if (error != null) {
                mOnError(error)
                return@addGroupMember
            }

            if (result?.status == "true") {
                infoToast(result.message)


                val date = (System.currentTimeMillis() / 1000).toString()
                val myObj = MySharedPreference(this).getUserObject()

                var dbConv = FirebaseDatabase.getInstance().reference
                    .child("Conversation")

                dbConv.child(
                    myObj?.id?.toString() ?: "0"
                ).child(group_id).setValue(
                    ChatHeadsModel(
                        group_id, date, "", true, name, "Group joined"
                    )
                )

                val dbChat = FirebaseDatabase.getInstance().reference
                    .child("Chat")
                    .child("Group")
                    .child(group_id)
                    .child("Members")

                dbChat.child(myObj?.id?.toString() ?: "0").setValue(
                    AddMemberFirebase(
                        myObj?.id?.toString() ?: "0",
                        myObj?.detail?.profile_image ?: "",
                        myObj?.first_name ?: "" + " " + myObj?.last_name ?: ""
                    )
                )

                getPublicGroups()
                getMyGroups()

            }
        }
    }

    private fun groupDelete(position: Int) {
        val group_id = myGroups[position].id

        pBar(1)
        APIManager.deleteGroup(returnUserAuthToken(), group_id.toString()) { result, error ->
            pBar(0)
            if (error != null) {
                mOnError(error)
                return@deleteGroup
            } else if (result!!.status == "true") {
                successToast(result.message)

                deleteFireBase(group_id)
                getMyGroups()
            }

        }
    }

    private fun deleteFireBase(group_id: Int) {
        //Chat.group.{groupid}
        //  FirebaseDatabase.getInstance().reference.child("Conversation").child(MySharedPreference(this).getUserObject()!!.id.toString()).child(group_id.toString()).removeValue()
        FirebaseDatabase.getInstance().reference.child("Chat").child("Group")
            .child(group_id.toString())
            .child("Members").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (member in snapshot.children) {
                        //Conversation.userid.{groupid}
                        FirebaseDatabase.getInstance().reference.child("Conversation")
                            .child(member.key.toString()).child(group_id.toString()).removeValue()
                        FirebaseDatabase.getInstance().reference.child("Chat").child("Group")
                            .child(group_id.toString()).removeValue()
                        //   Log.d("shahzain", member.key.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

}