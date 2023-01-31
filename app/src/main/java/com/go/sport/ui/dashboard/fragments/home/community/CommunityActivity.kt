package com.go.sport.ui.dashboard.fragments.home.community

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.creategroup.firebase.AddMemberFirebase
import com.go.sport.model.firebase.ChatHeadsModel
import com.go.sport.model.getusers.GetUsersData
import com.go.sport.model.unjoinedgroups.ListUnjoinedData
import com.go.sport.network.APIManager
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.activities.FilterModel
import com.go.sport.ui.dashboard.chats.ConversationActivity
import com.go.sport.ui.dashboard.fragments.home.adapter.ComunityGroupAdapter
import com.go.sport.ui.dashboard.fragments.home.community.adapter.BookFilterAdapter
import com.go.sport.ui.dashboard.fragments.home.community.adapter.CommunityAdapter
import com.go.sport.ui.dashboard.playerprofile.PlayerProfileActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jakewharton.rxbinding2.view.RxView
import com.mohammedalaa.seekbar.DoubleValueSeekBarView
import com.mohammedalaa.seekbar.OnDoubleValueSeekBarChangeListener
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_community.iv_back
import kotlinx.android.synthetic.main.activity_community.tv_filter
import kotlinx.android.synthetic.main.activity_community.tv_title
import kotlinx.android.synthetic.main.addgroup.*
import kotlinx.android.synthetic.main.bottom_sheet_community.*
import kotlinx.android.synthetic.main.fragment_select_attributes.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class CommunityActivity : BaseActivity(),
    CommunityAdapter.CommunityAdapterClickListener {

    private var myGroups = ArrayList<ListUnjoinedData>()
    private lateinit var bsd: BottomSheetDialog

    private val filterIds = ArrayList<Int>()
    private val users = arrayListOf<GetUsersData>()
    private val copiedUsers = arrayListOf<GetUsersData>()


    var mode = ""
    private var gender = ""
    private var minAge = ""
    private var maxAge = ""
    private var age = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        if (intent.hasExtra("mode")) {
            mode = intent.getStringExtra("mode").toString()
            tv_title.text = "Members"
            tv_filter.visibility = GONE
            if (mode == "chat") {
                tv_filter.visibility = GONE
                tv_title.text = "Select User"
            }
        }

        initViews()
        initBottomSheet()
        initListeners()
        mGetUsers()
    }

    private fun initViews() {
        rv_community.adapter = CommunityAdapter(this, this, users, mode)
    }

    @SuppressLint("CheckResult")
    private fun initBottomSheet() {
        bsd = BottomSheetDialog(this)
        bsd.setContentView(R.layout.bottom_sheet_community)
        bsd.cont_sports.visibility = GONE
        bsd.cont_age_bar.visibility = VISIBLE
        bsd.view_2.visibility = GONE
        bsd.view_3.visibility = GONE

        bsd.rv_sports.apply {
            adapter = BookFilterAdapter(
                this@CommunityActivity,
                arrayListOf(
                    FilterModel("1", "Football"),
                    FilterModel("2", "Cricket"),
                    FilterModel("3", "Badminton"),
                    FilterModel("4", "Volleyball"),
                    FilterModel("5", "Tennis"),
                    FilterModel("6", "Hockey")
                ),
                filterIds,
                object : BookFilterAdapter.OnFiltersClickListener {
                    override fun onFilterItemClick(position: Int, filter: FilterModel) {
                        if (!filterIds.contains(filter.id.toInt())) {
                            filterIds.add(filter.id.toInt())
                        } else {
                            filterIds.remove(filter.id.toInt())
                        }

                        // changing bottom sheet button and text color
                        if (filterIds.size > 0) {
                            bsd.cont_apply.background =
                                ResourcesCompat.getDrawable(
                                    this@CommunityActivity.resources,
                                    R.drawable.bg_grad_purple,
                                    null
                                )
                            bsd.tv_apply.setTextColor(
                                ContextCompat.getColor(
                                    this@CommunityActivity,
                                    R.color.white
                                )
                            )
                        } else {
                            bsd.cont_apply.background =
                                ResourcesCompat.getDrawable(
                                    this@CommunityActivity.resources,
                                    R.drawable.bg_grey,
                                    null
                                )
                            bsd.tv_apply.setTextColor(
                                ContextCompat.getColor(
                                    this@CommunityActivity,
                                    R.color.grey_2
                                )
                            )
                        }

//                        getAgeRangeAndPlayersCount()
                        bsd.rv_sports.adapter?.notifyDataSetChanged()
                    }
                }
            )
        }

        RxView.clicks(bsd.cont_male).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            gender = "male"
            changeSelection(
                bsd.cont_male,
                bsd.tv_male,
                bsd.cont_female,
                bsd.tv_female,
                bsd.cont_unisex,
                bsd.tv_unisex
            )
        }

        RxView.clicks(bsd.cont_female).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            gender = "female"
            changeSelection(
                bsd.cont_female,
                bsd.tv_female,
                bsd.cont_male,
                bsd.tv_male,
                bsd.cont_unisex,
                bsd.tv_unisex
            )
        }

        RxView.clicks(bsd.cont_unisex).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            gender = "both"
            changeSelection(
                bsd.cont_unisex,
                bsd.tv_unisex,
                bsd.cont_male,
                bsd.tv_male,
                bsd.cont_female,
                bsd.tv_female
            )
        }

        RxView.clicks(bsd.cont_apply).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (bsd.isShowing) {
                if (gender.isEmpty()) {
                    warningToast("Please select any gender to continue")
                    return@subscribe
                }

            }

                bsd.double_range_seekbar.setOnRangeSeekBarViewChangeListener(object :
                    OnDoubleValueSeekBarChangeListener {
                    override fun onStartTrackingTouch(
                        seekBar: DoubleValueSeekBarView?,
                        min: Int,
                        max: Int
                    ) {

                    }

                    override fun onStopTrackingTouch(seekBar: DoubleValueSeekBarView?, min: Int, max: Int) {

                    }

                    override fun onValueChanged(
                        seekBar: DoubleValueSeekBarView?,
                        min: Int,
                        max: Int,
                        fromUser: Boolean
                    ) {
                        minAge = min.toString()
                        maxAge = max.toString()
                    }
                })

            minAge = bsd.double_range_seekbar.currentMinValue.toString()
            maxAge = bsd.double_range_seekbar.currentMaxValue.toString()
            for (user in users){
                val ageArr = user.detail.date_of_birth.split("-")
                minAge = getAgeFromDOB(ageArr[0].toInt(),ageArr[1].toInt(),ageArr[2].toInt()).toString()
                maxAge = getAgeFromDOB(ageArr[0].toInt(),ageArr[1].toInt(),ageArr[2].toInt()).toString()
            }
            age = "${this.minAge}, ${this.maxAge}"

            mFilterUsers()
            bsd.dismiss()
            filter(et_search.text.toString())


//            for(user in users){
//                val ageArr = users.detail.date_of_birth.split("-")
//                val age = getAgeFromDOB(ageArr[0].toInt(),ageArr[1].toInt(),ageArr[2].toInt())!!.toInt()
//                if(minAge>age || maxAge<age){
//                    users.remove(user)
//                }
//                else if (user.detail.gender.toLowerCase() != gender.toLowerCase()){
//                    users.remove(user)
//                }
//
//            }
            rv_community.adapter!!.notifyDataSetChanged()


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

        RxView.clicks(tv_filter).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (!bsd.isShowing)
                bsd.show()
        }

        et_search.doOnTextChanged { text, _, _, _ ->
            filter(text.toString())
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

    private fun changeSelection(
        viewToSelect: View,
        textViewToSelect: TextView,
        viewToDeselectOne: View,
        textViewToDeselectOne: TextView,
        viewToDeselectTwo: View?,
        textViewToDeselectTwo: TextView?
    ) {
        viewToSelect.background =
            ContextCompat.getDrawable(this, R.drawable.bg_grad_purple)
        textViewToSelect.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        viewToDeselectOne.background =
            ContextCompat.getDrawable(this, R.drawable.stroke_purple_5_radius)
        textViewToDeselectOne.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.purple_dark_3
            )
        )
        viewToDeselectTwo?.background =
            ContextCompat.getDrawable(this, R.drawable.stroke_purple_5_radius)
        textViewToDeselectTwo?.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.purple_dark_3
            )
        )
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
                //   result.data?.let { users.addAll(it) }
                //  result.data?.let { copiedUsers.addAll(it) }

                for (user in result.data!!) {
                    if (user.detail != null) {
                        users.add(user)
                    }
                }
                copiedUsers.addAll(users)

                /*users.removeAt(0)
                users.removeAt(0)
                copiedUsers.removeAt(0)
                copiedUsers.removeAt(0)*/

                rv_community.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun mFilterUsers() {
        pBar(1)
        APIManager.communityFilter(returnUserAuthToken(), gender, age) { result, error ->
            pBar(0)
            users.clear()
            copiedUsers.clear()
            rv_community.adapter?.notifyDataSetChanged()

            if (error != null) {
                mOnError(error)
                return@communityFilter
            }

            if (result?.status == true) {
                result.user?.let { users.addAll(it) }
                result.user?.let { copiedUsers.addAll(it) }
                rv_community.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onCommunityItemClick(user: GetUsersData) {
        startActivity(
            this,
            PlayerProfileActivity::class.java,
            false,
            1,
            bundleOf(Pair("user_id", user.id.toString()))
        )
    }

    override fun onCommunityItemAddClick(position: Int) {

        val bsd = BottomSheetDialog(this)
        bsd.setContentView(R.layout.addgroup)
        bsd.rv_mygroups.adapter =
            ComunityGroupAdapter(this, myGroups, object : ComunityGroupAdapter.onItemClickListener {
                override fun onItemSelected(pos: Int) {

                    addGroupMember(
                        myGroups[pos].id.toString(),
                        users[position].id.toString(),
                        myGroups[pos].name,
                        bsd
                    )


                }
            })
        getMyGroups(bsd)
    }

    override fun onCommunityItemChatClick(position: Int, member: GetUsersData) {


        FirebaseDatabase.getInstance().reference
            .child("Conversation")
            .child(MySharedPreference(this).getUserObject()!!.id.toString())
            .child(member.id.toString())
            .child("blocked").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == "true") {

                        warningToast("You can't send message to this user")
                    } else {
                        val bundle = Bundle()
                        bundle.putParcelable(
                            "userObject", ChatHeadsModel(
                                member.id.toString(), System.currentTimeMillis().toString(),
                                member.detail?.profile_image ?: "", false,
                                member.first_name + " " + member.last_name, ""
                            )
                        )
                        startActivity(
                            this@CommunityActivity,
                            ConversationActivity::class.java,
                            false,
                            1,
                            bundle
                        )
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun getMyGroups(bsd: BottomSheetDialog) {
        pBar(1)
        APIManager.listJoinedGroups(returnUserAuthToken()) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@listJoinedGroups
            }

            if (result?.status == true) {
                bsd.show()
                myGroups.clear()
                myGroups.addAll(result.data)
                bsd.rv_mygroups.adapter?.notifyDataSetChanged()
            }
        }
    }

    fun addGroupMember(
        group_id: String,
        memberId: String,
        name: String,
        bsd: BottomSheetDialog
    ) {

        pBar(1)
        APIManager.addGroupMember(returnUserAuthToken(), group_id, memberId) { result, error ->
            pBar(0)



            if (error != null) {
                mOnError(error)
                return@addGroupMember
            }

            if (result?.status == "true") {
                infoToast(result.message)
                bsd.dismiss()


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

            }
        }
    }


}