package com.go.sport.ui.signup.fragments

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.constants.Constants
import com.go.sport.ui.signup.SignUpActivity
import com.go.sport.ui.signup.SignUpSingleton
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_phone_number.view.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class PhoneNumberFragment : Fragment() {

    private lateinit var rootView: View

    private var countryCode = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_phone_number, container, false)

        countryCode = "+${rootView.ccp.selectedCountryCode}"

        setFonts()
        initListeners()

        return rootView
    }

    private fun setFonts() {
        (activity as BaseActivity).setFont(Constants.REGULAR, rootView.til_phone_number)
        rootView.ccp.typeFace =
            Typeface.createFromAsset(requireActivity().assets, Constants.REGULAR)
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        rootView.ccp.setOnCountryChangeListener {
            countryCode = "+${it.phoneCode}"
        }

        RxView.clicks(rootView.cont_continue).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (activity as SignUpActivity).view_pager.setCurrentItem(
                (activity as SignUpActivity).view_pager.currentItem + 1,
                true
            )
        }
    }

    fun initPhoneNumberValidation(): Boolean {
        val phoneNumber = rootView.et_phone_number.text.toString()

        if (phoneNumber.isBlank() || phoneNumber.isEmpty()) {
            rootView.et_phone_number.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Invalid Phone Number")
            return false
        }

        if (phoneNumber.isBlank() || phoneNumber.isEmpty()) {
            rootView.et_phone_number.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Invalid Phone Number")
            return false
        }

        if (phoneNumber.length < 9) {
            rootView.et_phone_number.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Phone Number should not be less than 9 digits")
            return false
        }

        SignUpSingleton.countryCode = countryCode
        SignUpSingleton.phoneNumber = phoneNumber

        return true
    }
}