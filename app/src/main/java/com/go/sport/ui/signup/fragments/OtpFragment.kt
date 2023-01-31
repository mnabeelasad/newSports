package com.go.sport.ui.signup.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.ui.signup.SignUpActivity
import com.go.sport.ui.signup.SignUpSingleton
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_otp.view.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class OtpFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_otp, container, false)

        initViews()
        initListeners()

        return rootView
    }

    fun setPhoneNumberText() {
        rootView.tv_verification.text =
            "We have sent you SMS with 4 digits \nverification code (OTP) on ${SignUpSingleton.countryCode} ${SignUpSingleton.phoneNumber}"
    }

    private fun initViews() {
        rootView.pin_view.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.purple_dark
            )
        )
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        rootView.pin_view.setPinViewEventListener { pinView, _ ->
            if (pinView.value.length == 4)
                SignUpSingleton.otp = pinView.value
        }

        RxView.clicks(rootView.cont_verify).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (activity as SignUpActivity).view_pager.setCurrentItem(
                (activity as SignUpActivity).view_pager.currentItem + 1,
                true
            )
        }
    }

    fun initOtpValidation(): Boolean {
        if (SignUpSingleton.otp.length < 4) {
            (requireActivity() as BaseActivity).warningToast("OTP should not be less than 4 digits")
            return false
        }

        return true
    }
}