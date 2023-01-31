package com.go.sport.ui.signup.fragments

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.ui.signup.SignUpActivity
import com.go.sport.ui.signup.SignUpSingleton
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_gender.view.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class GenderFragment : Fragment() {

    private lateinit var rootView: View
    private var isMaleSelected = false
    private var isFemaleSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_gender, container, false)

        initListeners()

        return rootView
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(rootView.cont_male).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            SignUpSingleton.gender = "male"

            (activity as BaseActivity).imageViewAnimatedChange(
                rootView.iv_male,
                BitmapFactory.decodeResource(
                    requireContext().resources,
                    R.drawable.icon_male_selected
                )
            )

            if (isFemaleSelected)
                (activity as BaseActivity).imageViewAnimatedChange(
                    rootView.iv_female,
                    BitmapFactory.decodeResource(
                        requireContext().resources,
                        R.drawable.icon_female_unselected
                    )
                )

            isMaleSelected = true
            isFemaleSelected = false
        }

        RxView.clicks(rootView.cont_female).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            SignUpSingleton.gender = "female"

            (activity as BaseActivity).imageViewAnimatedChange(
                rootView.iv_female,
                BitmapFactory.decodeResource(
                    requireContext().resources,
                    R.drawable.icon_female_selected
                )
            )

            if (isMaleSelected)
                (activity as BaseActivity).imageViewAnimatedChange(
                    rootView.iv_male,
                    BitmapFactory.decodeResource(
                        requireContext().resources,
                        R.drawable.icon_male_unselected
                    )
                )

            isMaleSelected = false
            isFemaleSelected = true
        }

        RxView.clicks(rootView.cont_continue).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (activity as SignUpActivity).view_pager.setCurrentItem(
                (activity as SignUpActivity).view_pager.currentItem + 1,
                true
            )
        }
    }

    fun initGenderValidation(): Boolean {
        if (SignUpSingleton.gender.isBlank() || SignUpSingleton.gender.isEmpty()) {
            (requireActivity() as BaseActivity).warningToast("No Gender Selected, Please Select One")
            return false
        }

        return true
    }
}