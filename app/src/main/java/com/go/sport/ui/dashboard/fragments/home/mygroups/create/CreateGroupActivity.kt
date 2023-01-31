package com.go.sport.ui.dashboard.fragments.home.mygroups.create

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdacciaro.iOSDialog.iOSDialogBuilder
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.constants.Constants
import com.go.sport.model.creategroup.Group
import com.go.sport.model.creategroup.firebase.AddMemberFirebase
import com.go.sport.model.firebase.ChatHeadsModel
import com.go.sport.model.getusers.GetUsersData
import com.go.sport.network.APIManager
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.ui.dashboard.activities.FilterModel
import com.go.sport.ui.dashboard.chats.ConversationActivity
import com.go.sport.ui.dashboard.fragments.home.mygroups.create.adapter.AddedUsersAdapter
import com.go.sport.ui.dashboard.fragments.home.mygroups.create.adapter.BookFilterAdapter
import com.go.sport.ui.dashboard.fragments.home.mygroups.create.adapter.UsersAdapter
import com.go.sport.ui.dashboard.playerprofile.PlayerProfileActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.activity_create_group.iv_back
import kotlinx.android.synthetic.main.activity_create_group.iv_search
import kotlinx.android.synthetic.main.bottom_sheet_search.*
import kotlinx.android.synthetic.main.bottom_sheet_search.cont_search
import kotlinx.android.synthetic.main.bottom_sheet_search.til_search
import kotlinx.android.synthetic.main.bsd_creategroup.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class CreateGroupActivity : BaseActivity(),
    UsersAdapter.UsersAdapterClickListener,
    AddedUsersAdapter.AddedUsersClickListener {

    private var users = ArrayList<GetUsersData>()
    private var usersCopy = ArrayList<GetUsersData>()
    private lateinit var bsd: BottomSheetDialog
    private lateinit var createGroupbsd: BottomSheetDialog


    private val addedUsers = arrayListOf<GetUsersData>()
    private val bottomSheetFilterIds = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        initViews()
        initBottomSheet()
        groupNameBSD()
        initListeners()
    }

    private fun initViews() {
        rv_users_added.apply {
            layoutManager =
                LinearLayoutManager(this@CreateGroupActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter =
                AddedUsersAdapter(this@CreateGroupActivity, addedUsers, this@CreateGroupActivity)
        }

        rv_users.adapter = UsersAdapter(this, users, this)
        mGetUsers()
    }

    private fun mGetUsers() {
        pBar(1)
        APIManager.getUsers(returnUserAuthToken()) { result, error ->
            pBar(0)

            users.clear()
            usersCopy.clear()
            rv_users.adapter?.notifyDataSetChanged()

            if (error != null) {
                mOnError(error)
                return@getUsers
            }

            if (result?.status == true) {
                result.data?.let { users.addAll(it) }
                result.data?.let { usersCopy.addAll(it) }
                rv_users.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun createGroup(groupName: String, members: String, bsd: BottomSheetDialog) {
        pBar(1)
        APIManager.createGroup(returnUserAuthToken(), groupName, members) { result, error ->
            pBar(0)


            if (error != null) {
                mOnError(error)
                return@createGroup
            }

            if (result?.status == "true") {

                //zain group
                firebaseCreateGroup(bsd, result.group, result.message)
                bsd.dismiss()

            }
        }
    }

    private fun firebaseCreateGroup(
        bsd: BottomSheetDialog,
        group: Group,
        message: String
    ) {
//region


        val date = (System.currentTimeMillis() / 1000).toString()
        val myObj = MySharedPreference(this).getUserObject()

        var dbConv = FirebaseDatabase.getInstance().reference
            .child("Conversation")

        dbConv.child(
            myObj?.id?.toString() ?: "0"
        ).child(group.id.toString()).setValue(
            ChatHeadsModel(
                group.id.toString(), date, "", true, group.name, "Group Created"
            )
        )

        for (item in addedUsers) {
            dbConv.child(
                item.id.toString()
            ).child(group.id.toString()).setValue(
                ChatHeadsModel(
                    group.id.toString(), date, "", true, group.name, "Group Created"
                )
            )
        }



        val dbChat = FirebaseDatabase.getInstance().reference
            .child("Chat")
            .child("Group")
            .child(group.id.toString())
            .child("Members")

        dbChat.child(myObj?.id?.toString() ?: "0").setValue(
            AddMemberFirebase(
                myObj?.id?.toString() ?: "0",
                myObj?.detail?.profile_image ?: "",
                myObj?.first_name ?: "" + " " + myObj?.last_name ?: ""
            )
        )
        for (item in addedUsers) {
            dbChat.child(item.id.toString()).setValue(
                AddMemberFirebase(
                    item.id.toString(),
                    item.detail.profile_image ?: "",
                    item.first_name + " " + item.last_name
                )
            )
        }

        var dialog = iOSDialogBuilder(this)
            .setTitle("Go Play")
            .setSubtitle(message)
            .setBoldPositiveLabel(true)
            .setCancelable(false)
            .setPositiveListener(
                "Ok"
            ) { dialog ->
                dialog.dismiss()
                finish(this, -1)


            }
            /* .setNegativeListener("Dismiss"
             ) { dialog -> dialog.dismiss() }*/
            .build().show()


        //endregion
    }


    private fun groupNameBSD() {
        createGroupbsd = BottomSheetDialog(this)
        createGroupbsd.setContentView(R.layout.bsd_creategroup)

        createGroupbsd.cont_search.setOnClickListener {

            if (createGroupbsd.et_group_name.text.isNullOrBlank()) {
                infoToast("Please enter group name")
                return@setOnClickListener
            }
            if (addedUsers.size == 0) {
                infoToast("Please select user(s)")
                return@setOnClickListener
            } else {
                var usersIds = ""
                for (item in addedUsers) {
                    usersIds = usersIds + "," + item.id
                }
                createGroup(createGroupbsd.et_group_name.text.toString(), usersIds, createGroupbsd)
            }

        }

        createGroupbsd.setOnShowListener(DialogInterface.OnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
                ?: return@OnShowListener
            bottomSheet.background = null
        })
    }

    private fun initBottomSheet() {
        bsd = BottomSheetDialog(this)
        bsd.setContentView(R.layout.bottom_sheet_search)

        setFont(Constants.REGULAR, bsd.til_search)

        bsd.cont_search.setOnClickListener {
            bsd.dismiss()
        }

        bsd.rv_sports.apply {
            adapter = BookFilterAdapter(
                this@CreateGroupActivity, arrayListOf(
                    FilterModel("1", "Football"),
                    FilterModel("2", "Cricket"),
                    FilterModel("3", "Badminton"),
                    FilterModel("4", "Volleyball"),
                    FilterModel("5", "Tennis"),
                    FilterModel("6", "Hockey")
                ),
                bottomSheetFilterIds,
                object : BookFilterAdapter.OnFiltersClickListener {
                    override fun onFilterItemClick(position: Int, filter: FilterModel) {
                        if (!bottomSheetFilterIds.contains(filter.id.toInt())) {
                            bottomSheetFilterIds.add(filter.id.toInt())
                        } else {
                            bottomSheetFilterIds.remove(filter.id.toInt())
                        }

                        // changing bottom sheet button and text color
                        /* if (bottomSheetFilterIds.size > 0) {
                             bsd.cont_search.background =
                                     ResourcesCompat.getDrawable(
                                             this@CreateGroupActivity.resources,
                                             R.drawable.bg_grad_purple,
                                             null
                                     )
                             bsd.tv_search.setTextColor(
                                     ContextCompat.getColor(
                                             this@CreateGroupActivity,
                                             R.color.white
                                     )
                             )
                         } else {
                             bsd.cont_search.background =
                                     ResourcesCompat.getDrawable(
                                             this@CreateGroupActivity.resources,
                                             R.drawable.bg_grey,
                                             null
                                     )
                             bsd.tv_search.setTextColor(
                                     ContextCompat.getColor(
                                             this@CreateGroupActivity,
                                             R.color.grey_2
                                     )
                             )
                         }*/

                        bsd.rv_sports.adapter?.notifyDataSetChanged()
                    }
                }
            )
        }

        bsd.setOnShowListener(DialogInterface.OnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
                ?: return@OnShowListener
            bottomSheet.background = null
        })
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(iv_search).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (!bsd.isShowing)
                bsd.show()
        }

        cont_create_group.setOnClickListener {
            if (!createGroupbsd.isShowing)
                createGroupbsd.show()
        }
        et_search_name.doOnTextChanged { text, _, _, _ ->
            filter(text.toString())
        }
    }

    private fun filter(text: String) {
        users.clear()
        if (text.isEmpty()) {
            users.addAll(usersCopy)
        } else {
            for (item in usersCopy) {
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
        rv_users.adapter?.notifyDataSetChanged()
    }

    private fun createGroupContVisibility() {
        if (addedUsers.size > 0)
            cont_create_group.visibility = View.VISIBLE
        else
            cont_create_group.visibility = View.GONE
    }

    override fun onUsersItemClick(position: Int) {
        startActivity(
            this@CreateGroupActivity,
            PlayerProfileActivity::class.java,
            false,
            1,
            bundleOf(Pair("user_id", users[position].detail.user_id))
        )

    }

    override fun onUsersItemAddClick(position: Int) {
        if (!users[position].isClicked) {
            users[position].isClicked = true
            rv_users.adapter?.notifyItemChanged(position)

            addedUsers.add(0, users[position])
            rv_users_added.adapter?.notifyItemInserted(0)
            rv_users_added.adapter?.notifyItemRangeChanged(0, addedUsers.size)

            createGroupContVisibility()
        } else {
            warningToast("Already added to group!")
        }
    }

    override fun onUsersItemChatClick(position: Int) {
        val bundle = Bundle()
        bundle.putParcelable(
            "userObject",
            ChatHeadsModel(
                users[position].detail.id.toString(),
                System.currentTimeMillis().toString(),
                "",
                true,
                users[position].first_name.capitalize() + " " + users[position].last_name.capitalize(),
                ""
            )
        )
        startActivity(this, ConversationActivity::class.java, false, 1, bundle)
    }

    /*override fun onAddedUsersItemClick(position: Int) {
        startActivity(
            this@CreateGroupActivity,
            PlayerProfileActivity::class.java,
            false,
            1,
            bundleOf(Pair("user_id", users[position].id))
        )
    }*/

    override fun onAddedUsersItemCrossClick(position: Int) {
        users.forEach { user ->
            if (addedUsers[position] == user) {
                user.isClicked = false
                return@forEach
            }
        }
        rv_users.adapter?.notifyDataSetChanged()

        addedUsers.remove(addedUsers[position])
        rv_users_added.adapter?.notifyItemRemoved(position)
        rv_users_added.adapter?.notifyItemRangeChanged(position, addedUsers.size)

        createGroupContVisibility()
    }
}