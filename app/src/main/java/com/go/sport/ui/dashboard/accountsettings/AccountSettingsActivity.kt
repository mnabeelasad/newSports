package com.go.sport.ui.dashboard.accountsettings

import android.annotation.SuppressLint
import android.os.Bundle
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.constants.Constants
import com.go.sport.ui.dashboard.accountsettings.changepassword.ChangePasswordActivity
import com.go.sport.ui.dashboard.editprofile.EditProfileActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_account_settings.*
import java.util.concurrent.TimeUnit

class AccountSettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        setFonts()
        initListeners()
        initBottomSheet(
            "Are you sure to delete your account ?",
            "Delete Account",
            object : OnBottomSheetDialogClickListener {
                override fun onDismissButtonClick() {
                    areYouSureBottomSheet.dismissWithAnimation
                }
            })
    }

    private fun setFonts() {
        setFont(Constants.REGULAR, til_change_password)
        setFont(Constants.REGULAR, til_edit_profile)
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(cont_change_password).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivity(this, ChangePasswordActivity::class.java, false, 1)
        }

        RxView.clicks(cont_edit_profile).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivity(this,EditProfileActivity::class.java, false,1)
        }
    }
}