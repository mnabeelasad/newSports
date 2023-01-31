package com.go.sport.ui.dashboard.accountsettings.changepassword

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.constants.Constants
import com.go.sport.network.APIManager
import com.go.sport.sharedpref.MySharedPreference
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_change_password.*
import java.util.concurrent.TimeUnit

class ChangePasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        setFonts()
        initListeners()
    }

    private fun setFonts() {
        setFont(Constants.REGULAR, til_old_password)
        setFont(Constants.REGULAR, til_new_password)
        setFont(Constants.REGULAR, til_confirm_password)
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(cont_save).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            mChangePassword()
        }
    }

    private fun mChangePassword() {
        val oldPassword = et_old_password.text.toString()
        val newPassword = et_new_password.text.toString()
        val confirmNewPassword = et_confirm_password.text.toString()

        if (oldPassword.isEmpty() || oldPassword.isBlank()) {
            et_old_password.requestFocus()
            warningToast("Old password incorrect")
            return
        }

        if (oldPassword.length < 6) {
            et_old_password.requestFocus()
            warningToast("Old password should not be less than 6 digits")
            return
        }

        if (newPassword.isEmpty() || newPassword.isBlank()) {
            et_new_password.requestFocus()
            warningToast("Invalid New Password")
            return
        }

        if (newPassword.length < 6) {
            et_new_password.requestFocus()
            warningToast("New password should not be less than 6 digits")
            return
        }

        if (confirmNewPassword.isEmpty() || confirmNewPassword.isBlank()) {
            et_confirm_password.requestFocus()
            warningToast("Invalid Confirm Password")
            return
        }

        if (confirmNewPassword != newPassword) {
            et_confirm_password.requestFocus()
            warningToast("Passwords Did Not Match")
            return
        }

        pBar(1)
        APIManager.changePassword(
            returnUserAuthToken(),
            oldPassword,
            newPassword,
            confirmNewPassword
        ) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@changePassword
            }

            if (result?.status == true) {
                successToast(result.message)

                Handler().postDelayed({
                    super.onBackPressed()
                }, 800)
            }
        }
    }
}