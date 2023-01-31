package com.go.sport.ui.signup.fragments

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.fragment_create_password.view.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class CreatePasswordFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_create_password, container, false)

        setFonts()
        initListeners()

        return rootView
    }

    private fun setFonts() {
        (activity as BaseActivity).setFont(Constants.REGULAR, rootView.til_create_password)
        (activity as BaseActivity).setFont(Constants.REGULAR, rootView.til_confirm_password)
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(rootView.cont_continue).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (activity as SignUpActivity).view_pager.setCurrentItem(
                (activity as SignUpActivity).view_pager.currentItem + 1,
                true
            )
        }
    }

    fun initCreatePasswordValidation(): Boolean {
        val password = rootView.et_create_password.text.toString()
        val confirmPassword = rootView.et_confirm_password.text.toString()

        if (password.isEmpty() || password.isBlank()) {
            rootView.et_create_password.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Invalid Password")
            return false
        }

        if (confirmPassword.isEmpty() || confirmPassword.isBlank()) {
            rootView.et_confirm_password.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Invalid Confirm Password")
            return false
        }

        if (confirmPassword != password) {
            rootView.et_confirm_password.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Passwords Did Not Match")
            return false
        }

        if (password.length < 6) {
            rootView.et_create_password.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Password should not be less than 6 digits")
            return false
        }

        SignUpSingleton.password = password
        SignUpSingleton.confirmPassword = confirmPassword

        return true
    }
}