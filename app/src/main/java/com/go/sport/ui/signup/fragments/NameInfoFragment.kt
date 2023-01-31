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
import kotlinx.android.synthetic.main.fragment_name_info.view.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class NameInfoFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_name_info, container, false)

        setFonts()
        initListeners()

        return rootView
    }

    private fun setFonts() {
        (activity as BaseActivity).setFont(Constants.REGULAR, rootView.til_first_name)
        (activity as BaseActivity).setFont(Constants.REGULAR, rootView.til_last_name)
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

    fun initNameValidation(): Boolean {
        val firstName = rootView.et_first_name.text.toString()
        val lastName = rootView.et_last_name.text.toString()

        if (!(requireActivity() as BaseActivity).nameRegex(firstName)) {
            rootView.et_first_name.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Name should contain only characters")
            return false
        }

        if (!(requireActivity() as BaseActivity).nameRegex(lastName)) {
            rootView.et_last_name.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Name should contain only characters")
            return false
        }

        SignUpSingleton.firstName = firstName
        SignUpSingleton.lastName = lastName

        return true
    }
}