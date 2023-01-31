package com.go.sport.ui.dashboard.editprofile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.constants.Constants
import com.go.sport.model.getmyprofile.GetMyProfileData
import com.go.sport.model.login.LoginDetail
import com.go.sport.model.login.LoginUser
import com.go.sport.network.APIManager
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.ui.dashboard.editprofile.selectsports.SelectSportsActivity
import com.go.sport.utils.DateInputMask
import com.jakewharton.rxbinding2.view.RxView
import com.skydoves.powermenu.PowerMenuItem
import kotlinx.android.synthetic.main.activity_edit_profile.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class EditProfileActivity : BaseActivity() {

    private var gender = ""
    private var isPictureUploaded = false
    private var shouldLoadDataFromApi = true

    private val selectedSportIds = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        birthdayValidation()
        setFonts()
        initListener()
    }

    override fun onResume() {
        super.onResume()

        if (shouldLoadDataFromApi)
            getMyProfile()
    }

    private fun setFonts() {
        setFont(Constants.REGULAR, til_first_name)
        setFont(Constants.REGULAR, til_last_name)
        setFont(Constants.REGULAR, til_dob)
        setFont(Constants.REGULAR, til_dd)
        setFont(Constants.REGULAR, til_mm)
        setFont(Constants.REGULAR, til_yyyy)
        setFont(Constants.REGULAR, til_gender)
        setFont(Constants.REGULAR, til_phone_number)
        setFont(Constants.REGULAR, til_sport_type)
    }

    private fun birthdayValidation() {
        DateInputMask(et_dob)
    }

    @SuppressLint("CheckResult")
    private fun initListener() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(cont_gender).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            popupDisplay(
                this,
                cont_gender,
                et_gender,
                false,
                arrayListOf(
                    PowerMenuItem("Male"),
                    PowerMenuItem("Female")
                ),
                POPUPDISPLAY_MATCHCONT,
                0
            ) { selectedText ->
                if (selectedText == "Male") {
                    gender = "male"
                } else if (selectedText == "Female") {
                    gender = "female"
                }
            }
        }

        RxView.clicks(cont_image).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        RxView.clicks(cont_sports_type).throttleFirst(2, TimeUnit.SECONDS).subscribe {

            updateUserProfile()
        }

        RxView.clicks(cont_update).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            updateUserProfile(true)
        }
    }

    private fun updateViews(data: GetMyProfileData) {
        selectedSportIds.clear()

        if (data.detail.profile_image.isNotBlank() || data.detail.profile_image.isNotEmpty()) {
            Glide.with(this).load(data.detail.profile_image).into(iv_user)
            isPictureUploaded = true
        } else {
            Glide.with(this).load(R.drawable.avatar).into(iv_user)
        }
        tv_user.text = data.first_name + " " + data.last_name
        et_first_name.setText(data.first_name)
        et_last_name.setText(data.last_name)
        et_dob.setText(data.detail.date_of_birth)
        if (data.detail.date_of_birth.isNotEmpty() || data.detail.date_of_birth.isNotBlank()) {
            val dob = data.detail.date_of_birth.split("-")
            et_yyyy.setText(dob[0])
            et_mm.setText(dob[1])
            et_dd.setText(dob[2])
        }
        et_gender.setText(data.detail.gender.capitalize())
        et_phone_number.setText(data.phone_number)
        et_sport_type.setText(data.sport.joinToString { it.name })

        data.sport.forEach {
            selectedSportIds.add(it.id.toString())
        }

        gender = data.detail.gender
    }

    private fun getMyProfile() {
        pBar(1)
        APIManager.myProfile(returnUserAuthToken()) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@myProfile
            }

            if (result?.status == true) {
                updateViews(result.data)
                var user = MySharedPreference(this).getUserObject()
                MySharedPreference(this).saveUserObject(
                    LoginUser(
                        result.data.created_at,
                        "",
                        LoginDetail(
                            result.data.detail.country_id,
                            result.data.detail.created_at,
                            result.data.detail.date_of_birth,
                            result.data.detail.gender,
                            result.data.detail.id,
                            result.data.detail.nick_name,
                            result.data.detail.profile_image,
                            result.data.detail.updated_at,
                            result.data.detail.user_id
                        ),
                        result.data.email,
                        "",
                        result.data.first_name,
                        result.data.id,
                        result.data.last_name,
                        user!!.token,
                        result.data.updated_at
                    )
                )
            }
        }
    }

    private fun updateUserProfile(isBack: Boolean = false) {
        val firstName = et_first_name.text.toString()
        val lastName = et_last_name.text.toString()
        val dob = et_dob.text.toString()
        val yyyy = et_yyyy.text.toString()
        val mm = et_mm.text.toString()
        val dd = et_dd.text.toString()
        val currentDate = "$dd-$mm-$yyyy"
        val finalDate = getCurrentDate("dd-MM-yyyy")
        val date1: Date
        val date2: Date
        val dates = SimpleDateFormat("dd-MM-yyyy")
        date1 = dates.parse(currentDate)
        date2 = dates.parse(finalDate)
        val difference: Long = Math.abs(date1.time - date2.time)
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        val year = differenceDates / 365
        val YearDifference = year
        val phoneNumber = et_phone_number.text.toString()

        val profilePicture = if (isPictureUploaded)
            RequestBody.create(
                MediaType.parse("image/*"),
                getFileFromBitmap(
                    (iv_user.drawable as BitmapDrawable).bitmap,
                    10
                )
            )
        else
            null

        if (profilePicture == null) {
            warningToast("Please upload picture to continue")
            return
        }


        if (!nameRegex(firstName)) {
            et_first_name.requestFocus()
            warningToast("Invalid First Name")
            return
        }

        if (!nameRegex(lastName)) {
            et_last_name.requestFocus()
            warningToast("Invalid Last Name")
            return
        }

        if (dd.isEmpty() || dd.isBlank()) {
            et_dd.requestFocus()
            warningToast("Invalid Birth Date")
            return
        }

        if (mm.isEmpty() || mm.isBlank()) {
            et_mm.requestFocus()
            warningToast("Invalid Birth Month")
            return
        }

        if (yyyy.isEmpty() || yyyy.isBlank()) {
            et_yyyy.requestFocus()
            warningToast("Invalid Birth Year")
            return
        }

        if (dd.length < 2) {
            et_dd.requestFocus()
            warningToast("Invalid Birth Date")
            return
        }

        if (mm.length < 2) {
            et_mm.requestFocus()
            warningToast("Invalid Birth Month")
            return
        }

        if (yyyy.length < 4) {
            et_yyyy.requestFocus()
            warningToast("Invalid Birth Year")
            return
        }


        if (!isValidDate("$dd-$mm-$yyyy")) {
            warningToast("Invalid Date Of Birth")
            return
        }

        if (YearDifference < 13) {
            warningToast("You Must Be 13 Years")
            return
        }

        if (gender.isEmpty() || gender.isBlank()) {
            et_gender.requestFocus()
            warningToast("Invalid Gender")
            return
        }

        pBar(1)
        APIManager.updateProfile(
            returnUserAuthToken(),
            profilePicture,
            stringToRequestBody(firstName),
            stringToRequestBody(lastName),
            stringToRequestBody(gender),
            stringToRequestBody("$yyyy-$mm-$dd")
        ) { result, error ->

            pBar(0)

            if (error != null) {
                mOnError(error)
                return@updateProfile
            }

            if (result?.status == true) {


                if (isBack) {
                    successToast(result.message)
                    getMyProfile()

                    /* Handler().postDelayed({
                         super.onBackPressed()
                     }, 800)*/
                } else {
                    startActivity(
                        this,
                        SelectSportsActivity::class.java,
                        false,
                        1,
                        bundleOf(Pair("selectedSports", selectedSportIds))
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        shouldLoadDataFromApi = false

        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                val bitmap =
                    MediaStore.Images.Media.getBitmap(
                        contentResolver,
                        fileUri
                    )
                imageViewAnimatedChange(
                    iv_user,
                    bitmap
                )
                isPictureUploaded = true

                /*//You can get File object from intent
                            val file:File = ImagePicker.getFile(data)

                            //You can also get File Path from intent
                            val filePath:String = ImagePicker.getFilePath(data)*/
            }
            ImagePicker.RESULT_ERROR -> {
                errorToast(ImagePicker.getError(data))
            }
            else -> {
                errorToast("Task Cancelled")
            }
        }
    }
}