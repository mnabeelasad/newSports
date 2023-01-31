package com.go.sport.ui.dashboard.chats

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.ImageQuality
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.constants.Constants
import com.go.sport.model.creategroup.firebase.AddMemberFirebase
import com.go.sport.model.firebase.ChatHeadsModel
import com.go.sport.model.firebase.chatmodel.ChatData
import com.go.sport.model.firebase.chatmodel.ChatModel
import com.go.sport.model.firebase.chatmodel.Sender
import com.go.sport.model.viewgroup.ViewGroupData
import com.go.sport.network.APIManager
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.ui.dashboard.chats.adapter.ConversationAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.*
import com.jakewharton.rxbinding2.view.RxView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.vanniktech.emoji.EmojiPopup
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.activity_conversation.iv_back
import kotlinx.android.synthetic.main.activity_conversation.tv_title
import kotlinx.android.synthetic.main.activity_my_groups.*
import kotlinx.android.synthetic.main.bottom_sheet_conversation.*
import kotlinx.android.synthetic.main.bottom_sheet_imageorvideo.*
import java.util.concurrent.TimeUnit

class ConversationActivity : BaseActivity(),
    ConversationAdapter.ConversationClickListener {

    private var members = ArrayList<ViewGroupData>()
    private lateinit var chatHead: ChatHeadsModel
    private lateinit var bsd: BottomSheetDialog
    private lateinit var database: DatabaseReference
    var userId = ""
    var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        setFont()
        initViews()
        initListeners()
        initBottomSheet()
    }

    private fun setFont() {
        setFont(Constants.REGULAR, et_msg)
    }

    private fun initViews() {
        userId = MySharedPreference(this).getUserObject()?.id.toString() ?: ""
        userName =
            MySharedPreference(this).getUserObject()?.first_name.toString() + " " + MySharedPreference(
                this
            ).getUserObject()?.last_name.toString()
        chatHead = intent.extras!!.getParcelable("userObject")!!
        rv_chat.apply {
            adapter = ConversationAdapter(
                this@ConversationActivity,
                this@ConversationActivity,
                chatHead,
                chatList,
                userId
            )
        }

        var type = if (chatHead.group)
            "Group"
        else
            "Individual"

        if (type == "Individual") {
            getConversation(type)
            FirebaseDatabase.getInstance().reference
                .child("Conversation")
                .child(chatHead.id)
                .child(userId)
                .child("blocked").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.value == "true"){
                            bsd.tv_block.text = "Unblock User"
                            convo_cont.visibility = View.GONE
                        }
                        else {
                            bsd.tv_block.text = "Block User"
                            convo_cont.visibility = View.VISIBLE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })


        } else {
            iv_more.visibility = View.GONE
            getConversationGroup(type)
            getGroupMembers(chatHead.id)

        }
        tv_title.setText(chatHead.name)
    }

    fun getGroupMembers(group_id: String?) {

        pBar(1)
        APIManager.viewGroup(returnUserAuthToken(), group_id!!) { result, error ->
            pBar(0)

            members.clear()


            if (error != null) {
                mOnError(error)
                return@viewGroup
            }

            if (result?.status == "true") {
                result.data?.let { members.addAll(it) }
            }
        }
    }


    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        val emojiPopup =
            EmojiPopup.Builder.fromRootView(findViewById(R.id.root))
                .setOnEmojiPopupDismissListener {
                    iv_smiley.setImageResource(R.drawable.icon_emoji)

                }.setOnEmojiPopupShownListener {
                    iv_smiley.setImageResource(R.drawable.icon_keyboard)

                }.build(et_msg)

        RxView.clicks(iv_more).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            bsd.show()
        }

        RxView.clicks(iv_smiley).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            emojiPopup.toggle()

        }

        RxView.clicks(iv_attach).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            attachBottomSheet()
        }


        RxView.clicks(iv_send).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            val text = et_msg.text.toString()
            if (text != null && text != "") {
                if (chatHead.group) {
                    callGroup()
                    sendGroupNotification(
                        chatHead.id,
                        text,
                        "New message in " + chatHead.name + " from " + userName
                    )
                } else {
                    callAll4()
                    sendUserNotification(chatHead.id, text, "New message from " + userName)

                }
                closeKeyboard()
            }
        }
    }

    private fun sendGroupNotification(group_id: String, message: String, title: String) {
        APIManager.sendGroupNotification(
            returnUserAuthToken(),
            group_id,
            message,
            title
        ) { result, error ->

            if (error != null) {
                mOnError(error)
                return@sendGroupNotification
            }
        }
    }


    private fun sendUserNotification(user_id: String, message: String, title: String) {
        APIManager.sendUserNotification(
            returnUserAuthToken(),
            user_id,
            message,
            title
        ) { result, error ->

            if (error != null) {
                mOnError(error)
                return@sendUserNotification
            }

        }
    }


    private fun initBottomSheet() {
        bsd = BottomSheetDialog(this)
        bsd.setContentView(R.layout.bottom_sheet_conversation)

        bsd.cont_block_user.setOnClickListener {

            blockUser()

            if (!bsd.tv_block.text.contains("Unblock"))
                blockUser()
            else {
                unBlockUser()
            }



            bsd.dismiss()
        }

        bsd.cont_leave_chat.setOnClickListener {
            deleteChat()
            bsd.dismiss()
            super.onBackPressed()
        }
        bsd.setOnShowListener(DialogInterface.OnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
                ?: return@OnShowListener
            bottomSheet.background = null
        })
    }


    private fun deleteChat() {
        FirebaseDatabase.getInstance().reference
            .child("Conversation")
            .child(userId)
            .child(chatHead.id).removeValue()

        FirebaseDatabase.getInstance().reference
            .child("Chat")
            .child("Individual")
            .child(userId)
            .child(chatHead.id).removeValue()

    }


    private fun attachBottomSheet() {
        val imageOrVideoBottomSheet = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
        imageOrVideoBottomSheet.setContentView(R.layout.bottom_sheet_imageorvideo)

        imageOrVideoBottomSheet.cancel.setOnClickListener {
            imageOrVideoBottomSheet.dismiss()
        }
        imageOrVideoBottomSheet.image.setOnClickListener {
            imagePicker()
            imageOrVideoBottomSheet.dismiss()
        }

        imageOrVideoBottomSheet.video.setOnClickListener {
            imageOrVideoBottomSheet.dismiss()
            val bsdVideo = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)
            bsdVideo.setContentView(R.layout.bottom_sheet_imageorvideo)

            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(object : MultiplePermissionsListener {
                    @SuppressLint("MissingPermission")
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (report?.areAllPermissionsGranted()!!) {
                            bsdVideo.image.text = "Camera"
                            bsdVideo.video.text = "Gallery"
                            bsdVideo.show()
                            bsdVideo.image.setOnClickListener {
                                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)

                                // start the video capture Intent
                                val CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 2
                                startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE)
                                bsdVideo.dismiss()

                            }
                            bsdVideo.video.setOnClickListener {
                                val i = Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                                )
                                val CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 2
                                startActivityForResult(i, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE)
                                bsdVideo.dismiss()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        token!!.continuePermissionRequest()
                    }

                }
                ).check()

            bsdVideo.cancel.setOnClickListener {
                bsdVideo.dismiss()
            }
        }

        imageOrVideoBottomSheet.show()
    }

    private fun imagePicker() {
        val options: Options = Options.init()
            .setRequestCode(100) //Request code for activity results
            .setCount(1) //Number of images to restrict selection count
            .setFrontfacing(false) //Front Facing camera on start
            .setImageQuality(ImageQuality.HIGH) //Image Quality
            //.setPreSelectedUrls(returnValue) //Pre selected Image Urls
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT) //Orientation
            .setPath("/pix/images") //Custom Path For Image Storage

        Pix.start(this, options)
    }

    override fun onConversationItemClick(position: Int) {

    }

    //four operations

    private fun callGroup() {
        var type = if (chatHead.group)
            "Group"
        else
            "Individual"

        val text = et_msg.text.toString()
        et_msg.setText("")
        val date = (System.currentTimeMillis() / 1000).toString()

        conversationGroup(date, text)

    }

    fun callAll4() {
        var type = if (chatHead.group)
            "Group"
        else
            "Individual"

        val text = et_msg.text.toString()
        et_msg.setText("")
        val date = (System.currentTimeMillis() / 1000).toString()
        conversationOwn(date, text)
        conversationOther(date, text)
        chatOwn(date, text, type)
        chatOther(date, text, type)
    }

    private fun chatOwn(date: String, text: String, type: String) {
        val db = FirebaseDatabase.getInstance().reference
            .child("Chat")
            .child(type)
            .child(userId)
            .child(chatHead.id)
            .child("Messages")

        val key: String = db.push().key.toString()

        db.child(key).setValue(
            ChatModel(
                ChatData(text), date, key, Sender(userId, userName), "text"
            )
        )
    }

    private fun chatOther(date: String, text: String, type: String) {
        val db = FirebaseDatabase.getInstance().reference
            .child("Chat")
            .child(type)
            .child(chatHead.id)
            .child(userId)
            .child("Messages")

        val key: String = db.push().key.toString()

        db.child(key).setValue(
            ChatModel(
                ChatData(text), date, key, Sender(userId, userName), "text"
            )
        )
    }

    private fun conversationOther(date: String, text: String) {


        FirebaseDatabase.getInstance().reference
            .child("Conversation")
            .child(chatHead.id)
            .child(userId)
            .setValue(
                ChatHeadsModel(
                    userId, date, "", false, userName, text, read=false
                )
            )
    }

    fun conversationOwn(date: String, text: String) {
        FirebaseDatabase.getInstance().reference
            .child("Conversation")
            .child(userId)
            .child(chatHead.id)
            .setValue(
                ChatHeadsModel(
                    chatHead.id, date, "", false, chatHead.name, text,read=true
                )
            )

    }

    fun blockUser() {
        /*   FirebaseDatabase.getInstance().reference
               .child("Conversation")
               .child(userId)
               .child(chatHead.id)
               .child("blocked").setValue("true")*/


        FirebaseDatabase.getInstance().reference
            .child("Conversation")
            .child(chatHead.id)
            .child(userId)
            .child("blocked").setValue("true")

    }

    fun unBlockUser() {


        FirebaseDatabase.getInstance().reference
            .child("Conversation")
            .child(chatHead.id)
            .child(userId)
            .child("blocked").setValue("false")

    }

    fun conversationGroup(date: String, text: String) {
        val date = (System.currentTimeMillis() / 1000).toString()
        val myObj = MySharedPreference(this).getUserObject()

        var dbConv = FirebaseDatabase.getInstance().reference
            .child("Conversation")

        dbConv.child(
            myObj?.id?.toString() ?: "0"
        ).child(chatHead.id).setValue(
            ChatHeadsModel(
                chatHead.id, date, "", true, chatHead.name, text
            )
        )

        for (item in members) {
            dbConv.child(
                item.member_id
            ).child(chatHead.id).setValue(
                ChatHeadsModel(
                    chatHead.id, date, "", true, chatHead.name, text
                )
            )
        }


        val dbChat = FirebaseDatabase.getInstance().reference
            .child("Chat")
            .child("Group")
            .child(chatHead.id)
            .child("Messages")

        val key: String = dbChat.push().key.toString()

        dbChat.child(key).setValue(
            ChatModel(
                ChatData(text), date, key, Sender(userId, userName), "text"
            )
        )

        /*val dbChat = FirebaseDatabase.getInstance().reference
            .child("Chat")
            .child("Group")
            .child(chatHead.id.toString())
            .child("Members")

        dbChat.child(myObj?.id?.toString() ?: "0").setValue(
            AddMemberFirebase(
                myObj?.id?.toString() ?: "0",
                myObj?.myBookingDetail?.profile_image ?: "",
                myObj?.first_name ?: "" + " " + myObj?.last_name ?: ""
            )
        )
        for (item in addedUsers) {
            dbChat.child(item.id.toString()).setValue(
                AddMemberFirebase(
                    item.id.toString(),
                    item.myBookingDetail?.profile_image ?: "",
                    item.first_name + " " + item.last_name
                )
            )
        }*/


    }


    var chatList: ArrayList<ChatModel> = ArrayList()
    fun getConversation(type: String) {

        val userId = MySharedPreference(this).getUserObject()?.id.toString() ?: ""

        database = FirebaseDatabase.getInstance().reference
            .child("Chat")
            .child(type)
            .child(userId)
            .child(chatHead.id)
            .child("Messages")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //   pBar(0, this@TraineeChatListingActivity)
                chatList.clear()
                dataSnapshot.children.mapNotNullTo(chatList) {
                    it.getValue(ChatModel::class.java)
                }


                rv_chat.adapter!!.notifyDataSetChanged()
                /*if (chatList.size > 0) {
                    tv_no_chats.visibility = View.GONE
                } else {
                    tv_no_chats.visibility = View.VISIBLE
                }*/
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //    pBar(0, this@TraineeChatListingActivity)
                Log.w("chatheads", "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(postListener)


    }

    private fun getConversationGroup(type: String) {

        database = FirebaseDatabase.getInstance().reference
            .child("Chat")
            .child(type)
            .child(chatHead.id)
            .child("Messages")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //   pBar(0, this@TraineeChatListingActivity)
                chatList.clear()
                dataSnapshot.children.mapNotNullTo(chatList) {
                    it.getValue(ChatModel::class.java)
                }


                rv_chat.adapter!!.notifyDataSetChanged()
                /*if (chatList.size > 0) {
                    tv_no_chats.visibility = View.GONE
                } else {
                    tv_no_chats.visibility = View.VISIBLE
                }*/
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //    pBar(0, this@TraineeChatListingActivity)
                Log.w("chatheads", "loadPost:onCancelled", databaseError.toException())
            }
        }
        database.addValueEventListener(postListener)

    }
}