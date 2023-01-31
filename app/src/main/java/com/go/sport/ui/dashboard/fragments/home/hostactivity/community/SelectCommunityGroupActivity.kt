package com.go.sport.ui.dashboard.fragments.home.hostactivity.community

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getusers.GetUsersData
import com.go.sport.model.unjoinedgroups.ListUnjoinedData
import com.go.sport.network.APIManager
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.fragments.home.hostactivity.community.adapter.CommunityAdapter
import com.go.sport.ui.dashboard.fragments.home.hostactivity.community.adapter.GroupAdapter
import com.google.android.material.tabs.TabLayout
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_select_community_group.*
import java.util.*
import kotlin.collections.ArrayList

class SelectCommunityGroupActivity : BaseActivity(),
    CommunityAdapter.OnCommunityClickListener,
    GroupAdapter.OnGroupClickListener {

    private val users = arrayListOf<GetUsersData>()
    private val copiedUsers = arrayListOf<GetUsersData>()
    private val copiedGroups = arrayListOf<ListUnjoinedData>()

    private var allGroups = ArrayList<ListUnjoinedData>()

    private var tabPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_community_group)

        Singleton.isInvitesActivityOpened = true
        mGetUsers()
        getAllGroups()
        initTabLayout()
        initRecyclerViews()
        initListeners()
    }

    private fun initTabLayout() {
        tab_layout.addTab(tab_layout.newTab().setText("Community"))
        tab_layout.addTab(tab_layout.newTab().setText("Groups"))
    }

    private fun initRecyclerViews() {
        rv_community.apply {
            adapter = CommunityAdapter(
                this@SelectCommunityGroupActivity,
                users,
                this@SelectCommunityGroupActivity
            )
        }

        rv_groups.apply {
            adapter = GroupAdapter(
                this@SelectCommunityGroupActivity,
                allGroups,
                this@SelectCommunityGroupActivity
            )
        }
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(cont_invite).subscribe {
            if (Singleton.userInvites.isNullOrEmpty() && Singleton.groupInvites.isNullOrEmpty() ) {
                errorToast("Please select user(s) or group(s) to continue")
                return@subscribe
            }
            else if (!Singleton.userInvites.isNullOrEmpty() && Singleton.groupInvites.isNullOrEmpty()){

                finish(this, -1)
            }
            else if(Singleton.userInvites.isNullOrEmpty() && !Singleton.groupInvites.isNullOrEmpty()){
                finish(this, -1)
            }
            if (Singleton.groupInvites.isNotEmpty() && Singleton.userInvites.isNotEmpty()) {
                finish(this, -1)
            }
        }

        et_search.doOnTextChanged { text, _, _, _ ->
            if (tabPos == 0)
                filter(text.toString())
            else
                filterGroup(text.toString())
        }

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPos = tab?.position ?: 0
                if (tab?.position ?: 0 == 0) {
                    rv_community.visibility = View.VISIBLE
                    rv_groups.visibility = View.GONE
                } else {
                    rv_groups.visibility = View.VISIBLE
                    rv_community.visibility = View.GONE
                }
            }
        })
    }

    private fun getAllGroups() {
        pBar(1)
        APIManager.listJoinedGroups(returnUserAuthToken()) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@listJoinedGroups
            }

            if (result?.status == true) {
                allGroups.clear()
                allGroups.addAll(result.data)
                rv_groups.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun mGetUsers() {
        pBar(1)
        APIManager.getUsers(returnUserAuthToken()) { result, error ->
            pBar(0)
            users.clear()
            copiedUsers.clear()
            rv_community.adapter?.notifyDataSetChanged()

            if (error != null) {
                mOnError(error)
                return@getUsers
            }

            if (result?.status == true) {
                result.data?.let { users.addAll(it) }
                result.data?.let { copiedUsers.addAll(it) }
                rv_community.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun filter(text: String) {
        users.clear()
        if (text.isEmpty()) {
            users.addAll(copiedUsers)
        } else {
            for (item in copiedUsers) {
                if (item.first_name.toLowerCase(Locale.getDefault())
                        .contains(text.toLowerCase(Locale.getDefault()))
                    ||
                    item.last_name.toLowerCase(Locale.getDefault())
                        .contains(text.toLowerCase(Locale.getDefault()))
                ) {
                    users.add(item)
                }
            }
        }
        rv_community.adapter?.notifyDataSetChanged()
    }

    private fun filterGroup(text: String) {
        allGroups.clear()
        if (text.isEmpty()) {
            allGroups.addAll(copiedGroups)
        } else {
            for (item in copiedGroups) {
                if (item.name.toLowerCase(Locale.getDefault())
                        .contains(text.toLowerCase(Locale.getDefault()))
                    ||
                    item.owner_name.toLowerCase(Locale.getDefault())
                        .contains(text.toLowerCase(Locale.getDefault()))
                ) {
                    allGroups.add(item)
                }
            }
        }
        rv_community.adapter?.notifyDataSetChanged()
    }

    override fun onCommunityItemClick(position: Int, filter: GetUsersData) {
        filter.isClicked = !filter.isClicked

        if (filter.isClicked) {
            Singleton.userInvites.add(filter.id.toString())
        } else {
            Singleton.userInvites.remove(filter.id.toString())
        }
        rv_community.adapter?.notifyDataSetChanged()
    }

    override fun onGroupItemClick(position: Int, filter: ListUnjoinedData) {
        filter.isSelected = !filter.isSelected

        if (filter.isSelected) {
            Singleton.groupInvites.add(filter.id.toString())
        } else {
            Singleton.groupInvites.remove(filter.id.toString())
        }
        rv_groups.adapter?.notifyDataSetChanged()
    }
}