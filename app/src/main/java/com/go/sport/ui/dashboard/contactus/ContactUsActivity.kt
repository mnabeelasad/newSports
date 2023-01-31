package com.go.sport.ui.dashboard.contactus

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import com.github.dhaval2404.imagepicker.ImagePicker
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.constants.Constants
import com.go.sport.network.APIManager
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.activity_contact_us.iv_back
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit

class ContactUsActivity : BaseActivity() {

    private var isPictureUploaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)

        setFonts()
        initListeners()
    }

    private fun setFonts() {
        setFont(Constants.REGULAR, til_name)
        setFont(Constants.REGULAR, til_subject)
        setFont(Constants.REGULAR, til_msg)
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(cont_send).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (isDataValid()) {
                contactUs()
            }
        }

        RxView.clicks(iv_upload).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .start { resultCode, data ->
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
                                iv_image,
                                bitmap
                            )

                            //*//*/You can get File object from intent
                            val file: File = ImagePicker.getFile(data)!!

                            //You can also get File Path from intent
                            val filePath: String = ImagePicker.getFilePath(data)!!
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                val bitmap =
                    MediaStore.Images.Media.getBitmap(
                        contentResolver,
                        fileUri
                    )

                iv_image.visibility = View.VISIBLE
                imageViewAnimatedChange(
                    iv_image,
                    bitmap
                )

                isPictureUploaded = true
            }
            ImagePicker.RESULT_ERROR -> {
                errorToast(ImagePicker.getError(data))
            }
            else -> {
                errorToast("Task Cancelled")
            }
        }
    }


    var image: RequestBody? = null
    private fun isDataValid(): Boolean {
        val isValid: Boolean

        val name = et_name.text.toString()
        val subject = et_subject.text.toString()
        val message = et_msg.text.toString()

        image = if (isPictureUploaded)
            RequestBody.create(
                MediaType.parse("image/*"),
                getFileFromBitmap(
                    (iv_image.drawable as BitmapDrawable).bitmap,
                    10
                )
            )
        else
            null

        when {
            name.isNullOrEmpty() -> {
                warningToast("Please enter name")
                et_name.requestFocus()
                isValid = false
            }
            subject.isNullOrEmpty() -> {
                warningToast("Please enter subject")
                et_subject.requestFocus()
                isValid = false
            }
            message.isNullOrEmpty() -> {
                warningToast("Please enter message")
                et_msg.requestFocus()
                isValid = false
            }
            else -> isValid = true
        }

        return isValid
    }

    private fun contactUs() {
        val name = et_name.text.toString()
        val subject = et_subject.text.toString()
        val message = et_msg.text.toString()


        pBar(1)

        APIManager.contactUs(
            returnUserAuthToken(),
            stringToRequestBody(name),
            stringToRequestBody(subject),
            stringToRequestBody(message),
            image
        ) { result, error ->

            pBar(0)

            if (result?.status == "true") {
                successToast(result.message)

                Handler().postDelayed({
                    super.onBackPressed()
                }, 800)
            }

            if (error != null) {
                mOnError(error)
                return@contactUs
            }
        }
    }
}