package com.go.sport.ui.signup.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.network.APIManager
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.signup.SignUpActivity
import com.go.sport.ui.signup.SignUpSingleton
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_upload_picture.view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 */
class UploadPictureFragment : Fragment() {

    private lateinit var rootView: View
    private var isPictureUploaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_upload_picture, container, false)

        initListeners()

        return rootView
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(rootView.cont_image).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()  //Final image resolution will be less than 1080 x 1080(Optional)
        }

        RxView.clicks(rootView.cont_upload_picture).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            uploadImage()
        }

        RxView.clicks(rootView.cont_skip).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            login()
        }
    }

    private fun login() {
        if ((requireActivity() as SignUpActivity).socialType.isNotEmpty()) {
            if ((requireActivity() as SignUpActivity).socialType == "facebook") {
                facebookLogin()
            } else if ((requireActivity() as SignUpActivity).socialType == "google") {
                googleLogin()
            }
        } else {
            loginUser()
        }
    }

    private fun startDashboardActivity() {
        (requireActivity() as BaseActivity).startActivityFinishAll(
            requireContext(),
            DashboardActivity::class.java,
            true,
            1
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                val bitmap =
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        fileUri
                    )
                (activity as BaseActivity).imageViewAnimatedChange(
                    rootView.iv_image,
                    bitmap
                )
                isPictureUploaded = true
            }
            ImagePicker.RESULT_ERROR -> {
                (activity as BaseActivity).errorToast(ImagePicker.getError(data))
            }
            else -> {
                (activity as BaseActivity).errorToast("Task Cancelled")
            }
        }
    }

    private fun uploadImage() {
        val profilePicture = if (isPictureUploaded)
            RequestBody.create(
                MediaType.parse("image/*"),
                (requireActivity() as BaseActivity).getFileFromBitmap(
                    (rootView.iv_image.drawable as BitmapDrawable).bitmap,
                    10
                )
            )
        else
            null

        if (profilePicture == null) {
            (requireActivity() as BaseActivity).warningToast("Please upload picture or press skip button to continue")
            return
        }

        val auth = HashMap<String, String>()
        auth["Authorization"] = (requireActivity() as SignUpActivity).bearerToken

        (requireActivity() as BaseActivity).pBar(1)
        APIManager.uploadUserImage(auth, profilePicture) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)

            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@uploadUserImage
            }

            if (result?.status == true)
                login()
        }
    }

    private fun loginUser() {
        (requireActivity() as BaseActivity).pBar(1)
        APIManager.login(
            SignUpSingleton.countryCode + SignUpSingleton.phoneNumber,
            SignUpSingleton.password
        ) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)

            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@login
            }

            if (result?.status == true) {
                MySharedPreference(requireContext()).saveUserObject(result.user)
                startDashboardActivity()
            }
        }
    }

    private fun facebookLogin() {
        (requireActivity() as BaseActivity).pBar(1)
        APIManager.facebookSignUp(
            (requireActivity() as SignUpActivity).emailAddress,
            (requireActivity() as SignUpActivity).socialToken
        ) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)

            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@facebookSignUp
            }

            if (result?.status == true) {
                MySharedPreference(requireContext()).saveUserObject(result.user)
                startDashboardActivity()
            }
        }
    }

    private fun googleLogin() {
        (requireActivity() as BaseActivity).pBar(1)
        APIManager.googleSignUp(
            (requireActivity() as SignUpActivity).emailAddress,
            (requireActivity() as SignUpActivity).socialToken
        ) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)

            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@googleSignUp
            }

            if (result?.status == true) {
                MySharedPreference(requireContext()).saveUserObject(result.user)
                startDashboardActivity()
            }
        }
    }
}