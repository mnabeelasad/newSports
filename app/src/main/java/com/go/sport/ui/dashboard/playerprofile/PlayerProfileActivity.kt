package com.go.sport.ui.dashboard.playerprofile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.firebase.ChatHeadsModel
import com.go.sport.model.userprofile.GetUserProfileData
import com.go.sport.model.userprofile.GetUserProfileSport
import com.go.sport.network.APIManager
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.ui.dashboard.chats.ConversationActivity
import com.go.sport.ui.dashboard.playerprofile.adapter.InterestAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.activity_player_profile.*
import kotlinx.android.synthetic.main.activity_player_profile.iv_back
import kotlinx.android.synthetic.main.bottom_sheet_conversation.*
import java.util.concurrent.TimeUnit

class PlayerProfileActivity : BaseActivity() {

    private var userData: GetUserProfileData? = null
    private val interests = arrayListOf<GetUserProfileSport>()
    private var userId = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_profile)

        userId = intent?.extras?.getString("user_id") ?: "0"
        getUserProfile()
        initViews()
        initListeners()
    }

    private fun initViews() {
        rv_interests.adapter = InterestAdapter(this, interests)
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(cont_send_message).throttleFirst(2, TimeUnit.SECONDS).subscribe {

            FirebaseDatabase.getInstance().reference
                .child("Conversation")
                .child(MySharedPreference(this).getUserObject()!!.id.toString())
                .child(userId)
                .child("blocked").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.value == "true") {

                            warningToast("You can't send message to this user")
                        } else {
                            val bundle = Bundle()
                            bundle.putParcelable(
                                "userObject", ChatHeadsModel(
                                    userId, System.currentTimeMillis().toString(),
                                    userData?.detail?.profile_image ?: "", false,
                                    userData?.first_name + " " + userData?.last_name, ""
                                )
                            )
                            startActivity(
                                this@PlayerProfileActivity,
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
    }

    private fun getUserProfile() {
        pBar(1)
        APIManager.userProfile(returnUserAuthToken(), userId) { result, error ->
            pBar(0)
            interests.clear()
            rv_interests.adapter?.notifyDataSetChanged()

            if (error != null) {
                mOnError(error)
                return@userProfile
            }

            if (result?.status == true) {

                userData = result.data
                val circularProgressDrawable = CircularProgressDrawable(this)
                circularProgressDrawable.centerRadius = 15f
                circularProgressDrawable.start()


                mediumTextView2.text = userData?.first_name + " " + userData?.last_name
                userData?.sport?.let { interests.addAll(it) }
                rv_interests.adapter?.notifyDataSetChanged()
                if (userData?.detail != null) {
                    if (userData?.detail?.profile_image != "") {
                        Glide.with(this).load(userData?.detail?.profile_image)
                            .placeholder(circularProgressDrawable).into(iv_image)
                    }else{
                        Glide.with(this).load(R.drawable.avatar)
                            .placeholder(circularProgressDrawable).into(iv_image)
                    }
                    tv_gender.text = userData?.detail?.gender?.capitalize()
                    tv_age.text = getAgeFromDOB(
                        userData?.detail?.date_of_birth?.split("-")?.get(0)?.toInt() ?: 0,
                        userData?.detail?.date_of_birth?.split("-")?.get(1)?.toInt() ?: 0,
                        userData?.detail?.date_of_birth?.split("-")?.get(2)?.toInt() ?: 0,
                    )
                }
            }
        }
    }
}