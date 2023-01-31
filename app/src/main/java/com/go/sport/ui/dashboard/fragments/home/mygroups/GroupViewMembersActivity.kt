package com.go.sport.ui.dashboard.fragments.home.mygroups

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.gdacciaro.iOSDialog.iOSDialogBuilder
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.viewgroup.ViewGroupData
import com.go.sport.network.APIManager
import com.go.sport.ui.dashboard.fragments.home.mygroups.adapter.GroupMembersAdapter
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_group_view_members.*
import kotlinx.android.synthetic.main.activity_group_view_members.iv_back
import kotlinx.android.synthetic.main.activity_group_view_members.rv_community
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class GroupViewMembersActivity : BaseActivity() , GroupMembersAdapter.GroupMembersClickListener {

    private var created_by = ""
    private var group_id = ""
    private var members = ArrayList<ViewGroupData>()
    private var membersCopy = ArrayList<ViewGroupData>()
    lateinit var groupMemberAdapter: GroupMembersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_view_members)


        if (intent.hasExtra("group_id")) {
            group_id = intent.getStringExtra("group_id")?: ""
            created_by = intent.getStringExtra("created_by")?: ""

            initViews()
            getGroupMembers(group_id)
        }
    }

    @SuppressLint("CheckResult")
    private fun initViews() {

        RxView.clicks(iv_back).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            super.onBackPressed()
        }

        et_search_user.doOnTextChanged { text, _, _, _ ->
            filter(text.toString())
        }

        groupMemberAdapter = GroupMembersAdapter(this, this, members, created_by)
        rv_community.adapter = groupMemberAdapter
    }

    private fun filter(text: String) {
        members.clear()
        if (text.isEmpty()) {
            members.addAll(membersCopy)
        } else {
            for (item in membersCopy) {
                if (item.user.first_name.toLowerCase(Locale.getDefault())
                        .contains(text.toLowerCase(Locale.getDefault()))
                    ||
                    item.user.last_name.toLowerCase(Locale.getDefault())
                        .contains(text.toLowerCase(Locale.getDefault()))
                ) {
                    members.add(item)
                }
            }
        }
        rv_community.adapter?.notifyDataSetChanged()
    }

    fun getGroupMembers(group_id: String?) {

        pBar(1)
        APIManager.viewGroup(returnUserAuthToken(), group_id!!) { result, error ->
            pBar(0)


            members.clear()
            membersCopy.clear()
            // copiedUsers.clear()
            rv_community.adapter?.notifyDataSetChanged()

            if (error != null) {
                mOnError(error)
                return@viewGroup
            }

            if (result?.status == "true") {
                result.data?.let { members.addAll(it) }
                result.data?.let { membersCopy.addAll(it) }
                rv_community.adapter?.notifyDataSetChanged()
            }
        }
    }
    fun removeGroupMember(group_id: String, memberId : String) {


        pBar(1)
        APIManager.removeGroupMember(returnUserAuthToken(), group_id, memberId) { result, error ->
            pBar(0)



            if (error != null) {
                mOnError(error)
                return@removeGroupMember
            }

            if (result?.status == "true") {
                infoToast(result.message)
                if (result.message == "Members deleted."){
                    successToast("Group Left Successfully")
                }
                 FirebaseDatabase.getInstance().reference
                    .child("Chat")
                    .child("Group")
                    .child(group_id)
                    .child("Members")
                    .child(memberId).removeValue()

                 FirebaseDatabase.getInstance().reference
                    .child("Conversation")
                    .child(memberId)
                    .child(group_id)
                     .removeValue()

                getGroupMembers(group_id)

            }
        }
    }

    override fun onGroupItemClick(position: Int) {

    }

    override fun onGroupItemAddClick(position: Int) {

        var dialog = iOSDialogBuilder(this)
            .setTitle("Go Play")
            .setSubtitle("Are you sure you want to leave Group?")
            .setBoldPositiveLabel(true)
            .setCancelable(false)
            .setPositiveListener(
                "Ok"
            ) { dialog ->
                dialog.dismiss()
                removeGroupMember(group_id, members[position].member_id)
            }
            .setNegativeListener("Cancel"
             ) { dialog -> dialog.dismiss() }
            .build().show()

    }

    override fun onGroupItemChatClick(position: Int) {

    }


}