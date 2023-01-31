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
import kotlinx.android.synthetic.main.fragment_birthday.view.*
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class BirthdayFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_birthday, container, false)

        setFonts()
        initListeners()

        return rootView
    }

    private fun setFonts() {
        (activity as BaseActivity).setFont(Constants.REGULAR, rootView.til_dd)
        (activity as BaseActivity).setFont(Constants.REGULAR, rootView.til_mm)
        (activity as BaseActivity).setFont(Constants.REGULAR, rootView.til_yyyy)
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

    private fun setDateMonthYear() {
        (requireActivity() as SignUpActivity).showDateOfBirthPicker(
            requireActivity(),
            rootView.et_dd
        )
    }

    fun initBirthdayValidation(): Boolean {
        val dd = rootView.et_dd.text.toString()
        val mm = rootView.et_mm.text.toString()
        val yyyy = rootView.et_yyyy.text.toString()
        val currentDate = "$dd-$mm-$yyyy"
        val finalDate = (activity as BaseActivity).getCurrentDate("dd-MM-yyyy")
        val date1: Date
        val date2: Date
        val dates = SimpleDateFormat("dd-MM-yyyy")
        date1 = dates.parse(currentDate)
        date2 = dates.parse(finalDate)
        val difference: Long = abs(date1.time - date2.time)
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        val year = differenceDates / 365
        val YearDifference = year
        if (dd.isEmpty() || dd.isBlank()) {
            rootView.et_dd.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Invalid Birth Day")
            return false
        }

        if (mm.isEmpty() || mm.isBlank()) {
            rootView.et_mm.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Invalid Birth Month")
            return false
        }

        if (yyyy.isEmpty() || yyyy.isBlank()) {
            rootView.et_yyyy.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Invalid Birth Year")
            return false
        }

        if (dd.length < 2) {
            rootView.et_dd.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Invalid Birth Day")
            return false
        }

        if (mm.length < 2) {
            rootView.et_mm.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Invalid Birth Month")
            return false
        }

        if (yyyy.length < 4) {
            rootView.et_yyyy.requestFocus()
            (requireActivity() as BaseActivity).warningToast("Invalid Birth Year")
            return false
        }

        if (!(requireActivity() as SignUpActivity).isValidDate("$dd-$mm-$yyyy")) {
            (requireActivity() as BaseActivity).warningToast("Invalid Date Of Birth")
            return false
        }
        if (YearDifference < 13){
            (requireActivity() as BaseActivity).warningToast("You Must Be 13 Years")
            return false
        }

        SignUpSingleton.birthDay = dd
        SignUpSingleton.birthMonth = mm
        SignUpSingleton.birthYear = yyyy

        return true
    }
}