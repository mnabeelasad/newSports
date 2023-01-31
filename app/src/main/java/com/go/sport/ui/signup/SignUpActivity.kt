package com.go.sport.ui.signup

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.custom.viewpager.transformer.DefaultSePageTransformer
import com.go.sport.custom.viewpager.transformer.SePageTransformer
import com.go.sport.network.APIManager
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.signup.adapter.ViewPagerAdapter
import com.go.sport.ui.signup.fragments.*
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class SignUpActivity : BaseActivity() {

    private val PHONE_NUMBER_FRAGMENT = 0
    private val OTP_FRAGMENT = 1
    private val EMAIL_FRAGMENT = 2
    private val NAME_INFO_FRAGMENT = 3
    private val BIRTHDAY_FRAGMENT = 4
    private val GENDER_FRAGMENT = 5
    private val CREATE_PASSWORD_FRAGMENT = 6
    private val SELECT_SPORTS_FRAGMENT = 7
    private val UPLOAD_PICTURE_FRAGMENT = 8



    private val phoneNumberFragment = PhoneNumberFragment()
    private val emailFragment = EmailFragment()
    private val otpFragment = OtpFragment()
    private val nameInfoFragment = NameInfoFragment()
    private val birthdayFragment = BirthdayFragment()
    private val genderFragment = GenderFragment()
    private val createFragment = CreatePasswordFragment()
    private val selectSportsFragment = SelectSportsFragment()
    private val uploadPictureFragment = UploadPictureFragment()

    private var isBackgroundChanged = false
    private var isBackArrowChanged = false

    var bearerToken = ""
    var emailAddress = ""
    var socialToken = ""
    var socialType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

//        fullScreen()
        init()
        initListeners()
    }

    private fun init() {
        emailAddress = intent?.extras?.getString("emailAddress") ?: ""
        socialToken = intent?.extras?.getString("token") ?: ""
        socialType = intent?.extras?.getString("socialType") ?: ""

        if (socialToken.isEmpty())
            setUpViewPager()
        else
            setUpViewPagerForSocial()
    }

    override fun onBackPressed() {
        popViewPagerFragment()
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            when (view_pager.currentItem) {

                EMAIL_FRAGMENT -> {
                    imageView2.visibility = View.VISIBLE
                    cancelToast("Are you sure you want to exit?")
                }
                NAME_INFO_FRAGMENT ->  {
                    imageView2.visibility = View.VISIBLE
                    iv_back.visibility = View.VISIBLE
                    view_pager.setCurrentItem(view_pager.currentItem - 1, true)
                }
                BIRTHDAY_FRAGMENT -> {
                    imageView2.visibility = View.VISIBLE
                    iv_back.visibility = View.VISIBLE
                    view_pager.setCurrentItem(view_pager.currentItem - 1, true)
                }
                GENDER_FRAGMENT -> {
                    imageView2.visibility = View.VISIBLE
                    iv_back.visibility = View.VISIBLE
                    view_pager.setCurrentItem(view_pager.currentItem - 1, true)
                }
                CREATE_PASSWORD_FRAGMENT -> {
                    imageView2.visibility = View.GONE
                    view_pager.setCurrentItem(view_pager.currentItem - 1, true)
                }
            }
        }

        RxView.clicks(iv_forward).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            if (view_pager.currentItem < UPLOAD_PICTURE_FRAGMENT) {
                if (socialToken.isEmpty()) {
                    when (view_pager.currentItem) {
                        PHONE_NUMBER_FRAGMENT -> if (phoneNumberFragment.initPhoneNumberValidation()) {
                            imageView2.visibility = View.GONE
                            iv_back.visibility = View.GONE
                            otpToPhoneNumber()
                        }
                        OTP_FRAGMENT -> if (otpFragment.initOtpValidation()) {
                            imageView2.visibility = View.VISIBLE
                            iv_back.visibility = View.GONE
                            mValidateOtp()
                        }
                        EMAIL_FRAGMENT -> if (emailFragment.initEmailValidation()) {
                            imageView2.visibility = View.VISIBLE
                            iv_back.visibility = View.VISIBLE
                            view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                        }
                        NAME_INFO_FRAGMENT -> if (nameInfoFragment.initNameValidation()) {
                            imageView2.visibility = View.VISIBLE
                            iv_back.visibility = View.VISIBLE
                            view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                        }
                        BIRTHDAY_FRAGMENT -> if (birthdayFragment.initBirthdayValidation()) {
                            imageView2.visibility = View.VISIBLE
                            iv_back.visibility = View.VISIBLE
                            view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                        }
                        GENDER_FRAGMENT -> if (genderFragment.initGenderValidation()) {
                            imageView2.visibility = View.VISIBLE
                            iv_back.visibility = View.VISIBLE
                            view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                        }
                        CREATE_PASSWORD_FRAGMENT -> if (createFragment.initCreatePasswordValidation()) {
                            imageView2.visibility = View.GONE
                            signUpUser()
                        }
                        SELECT_SPORTS_FRAGMENT -> if (selectSportsFragment.initSelectSportsValidation()) {
                            imageView2.visibility = View.GONE
                            mSelectSports()
                        }
                    }
                } else {
                    when (view_pager.currentItem) {
                        0 -> if (phoneNumberFragment.initPhoneNumberValidation()) {
                            imageView2.visibility = View.GONE
                            otpToPhoneNumber()
                        }
                        1 -> if (otpFragment.initOtpValidation()) {
                            imageView2.visibility = View.VISIBLE
                            mValidateOtp()
                        }
                        2 -> if (emailFragment.initEmailValidation()) {
                            imageView2.visibility = View.VISIBLE
                            iv_back.visibility = View.VISIBLE
                            view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                        }
                        3 -> if (nameInfoFragment.initNameValidation()) {
                            imageView2.visibility = View.VISIBLE
                            iv_back.visibility = View.VISIBLE
                            view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                        }
                        4 -> if (birthdayFragment.initBirthdayValidation()) {
                            imageView2.visibility = View.VISIBLE
                            iv_back.visibility = View.VISIBLE
                            view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                        }
                        5 -> if (genderFragment.initGenderValidation()) {
                            imageView2.visibility = View.GONE
                            iv_back.visibility = View.VISIBLE
                            signUpUserForSocial()
                        }
                        6 -> if (selectSportsFragment.initSelectSportsValidation()) {
                            imageView2.visibility = View.GONE
                            mSelectSports()
                        }
                    }
                }
            } else
                startActivityFinishAll(this, DashboardActivity::class.java, true, 1)
        }

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                changeImages(position)
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun setUpViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, 8)
        viewPagerAdapter.addFragment(PHONE_NUMBER_FRAGMENT, phoneNumberFragment)
        viewPagerAdapter.addFragment(OTP_FRAGMENT, otpFragment)
        viewPagerAdapter.addFragment(EMAIL_FRAGMENT, emailFragment)
        viewPagerAdapter.addFragment(NAME_INFO_FRAGMENT, nameInfoFragment)
        viewPagerAdapter.addFragment(BIRTHDAY_FRAGMENT, birthdayFragment)
        viewPagerAdapter.addFragment(GENDER_FRAGMENT, genderFragment)
        viewPagerAdapter.addFragment(CREATE_PASSWORD_FRAGMENT, createFragment)
        viewPagerAdapter.addFragment(SELECT_SPORTS_FRAGMENT, selectSportsFragment)
        viewPagerAdapter.addFragment(UPLOAD_PICTURE_FRAGMENT, uploadPictureFragment)

        view_pager.adapter = viewPagerAdapter
        view_pager.setScrollDuration(500)
        view_pager.setPagingEnabled(false)

        setUpViewPagerTransformer()
    }

    private fun setUpViewPagerTransformer() {
        val fragments =
            ArrayList<Fragment>()
        fragments.add(phoneNumberFragment)
        fragments.add(otpFragment)
        fragments.add(emailFragment)
        fragments.add(nameInfoFragment)
        fragments.add(birthdayFragment)
        fragments.add(genderFragment)
        fragments.add(createFragment)
        fragments.add(selectSportsFragment)
        fragments.add(uploadPictureFragment)

        val transformer: SePageTransformer =
            DefaultSePageTransformer(this, fragments, view_pager)
//        transformer.addTransition(R.id.tv_sign_up, R.id.tv_email_password)
//        transformer.addTransition(R.id.tv_email_password, R.id.tv_phone_address)
//        transformer.addTransition(R.id.tv_phone_address, R.id.tv_select_to_verify)
//        transformer.addTransition(R.id.tv_select_to_verify, R.id.tv_verification)
//        transformer.addTransition(R.id.tv_verification, R.id.tv_fitness_profile)

        view_pager.setPageTransformer(false, transformer)
        view_pager.addOnPageChangeListener(transformer)
    }

    private fun setUpViewPagerForSocial() {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, 8)
        viewPagerAdapter.addFragment(0, phoneNumberFragment)
        viewPagerAdapter.addFragment(1, otpFragment)
        viewPagerAdapter.addFragment(2, emailFragment)
        viewPagerAdapter.addFragment(3, nameInfoFragment)
        viewPagerAdapter.addFragment(4, birthdayFragment)
        viewPagerAdapter.addFragment(5, genderFragment)
        viewPagerAdapter.addFragment(6, selectSportsFragment)
        viewPagerAdapter.addFragment(7, uploadPictureFragment)

        view_pager.adapter = viewPagerAdapter
        view_pager.setScrollDuration(500)
        view_pager.setPagingEnabled(false)

        setUpViewPagerTransformerForSocial()
    }

    private fun setUpViewPagerTransformerForSocial() {
        val fragments = ArrayList<Fragment>()
        fragments.add(phoneNumberFragment)
        fragments.add(otpFragment)
        fragments.add(emailFragment)
        fragments.add(nameInfoFragment)
        fragments.add(birthdayFragment)
        fragments.add(genderFragment)
//        fragments.add(createFragment)
        fragments.add(selectSportsFragment)
        fragments.add(uploadPictureFragment)

        val transformer: SePageTransformer =
            DefaultSePageTransformer(this, fragments, view_pager)
//        transformer.addTransition(R.id.tv_sign_up, R.id.tv_email_password)
//        transformer.addTransition(R.id.tv_email_password, R.id.tv_phone_address)
//        transformer.addTransition(R.id.tv_phone_address, R.id.tv_select_to_verify)
//        transformer.addTransition(R.id.tv_select_to_verify, R.id.tv_verification)
//        transformer.addTransition(R.id.tv_verification, R.id.tv_fitness_profile)

        view_pager.setPageTransformer(false, transformer)
        view_pager.addOnPageChangeListener(transformer)
    }

    private fun popViewPagerFragment() {
        if (view_pager.currentItem >= 3)
            Handler().postDelayed({
                view_pager.animate()
                view_pager.setCurrentItem(
                    view_pager.currentItem - 1,
                    true
                )
            }, 40)
        else if (view_pager.currentItem == 0){
            finish()
        }
        else
            cancelToast("Are you sure you want to exit?")
    }

    private fun changeImages(position: Int) {
        if (position == OTP_FRAGMENT) {
            iv_back.visibility = View.GONE
            cont_wing.visibility = View.GONE
            cont_otp.visibility = View.VISIBLE

            tv_verification.text =
                "We have sent you SMS with 4 digits \nverification code (OTP) on ${SignUpSingleton.countryCode} ${SignUpSingleton.phoneNumber}"
        } else {
            //iv_back.visibility = View.VISIBLE
            cont_wing.visibility = View.VISIBLE
            cont_otp.visibility = View.GONE

            if (position == UPLOAD_PICTURE_FRAGMENT || (socialToken.isNotEmpty() && position == 7)) {
                // change back arrow and background on upload picture fragment and hide forward arrow
                iv_forward.visibility = View.GONE
                cont_wing.visibility = View.GONE

                isBackArrowChanged = true
                isBackgroundChanged = true

                imageViewAnimatedChange(
                    imageView,
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.bg_upload_picture
                    )
                )

                imageViewAnimatedChange(
                    iv_back,
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.arrow_left_dark
                    )
                )
            } else {
                iv_forward.visibility = View.VISIBLE

                if ((position == SELECT_SPORTS_FRAGMENT) || (socialToken.isNotEmpty() && position == 6))
                    cont_wing.visibility = View.GONE
                else
                    cont_wing.visibility = View.VISIBLE

                if (isBackgroundChanged)
                    imageViewAnimatedChange(
                        imageView,
                        BitmapFactory.decodeResource(
                            resources,
                            R.drawable.bg_purple_grad
                        )
                    )

                if (isBackArrowChanged)
                    imageViewAnimatedChange(
                        iv_back,
                        BitmapFactory.decodeResource(
                            resources,
                            R.drawable.arrow_left
                        )
                    )

                isBackArrowChanged = false
                isBackgroundChanged = false
            }
        }
    }

    private fun otpToPhoneNumber() {
        pBar(1)
        APIManager.sendOtpToPhoneNumber(SignUpSingleton.countryCode + SignUpSingleton.phoneNumber) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@sendOtpToPhoneNumber
            }

            if (result?.status == true) {
                view_pager.setCurrentItem(view_pager.currentItem + 1, true)
            }
        }
    }

    private fun mValidateOtp() {
        pBar(1)
        APIManager.validateOtp(
            SignUpSingleton.countryCode + SignUpSingleton.phoneNumber,
            SignUpSingleton.otp
        ) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@validateOtp
            }

            if (result?.status == true) {
                view_pager.setCurrentItem(view_pager.currentItem + 1, true)
            }
        }
    }

    private fun signUpUser() {
        pBar(1)

        APIManager.signUp(
            SignUpSingleton.firstName,
            SignUpSingleton.lastName,
            SignUpSingleton.emailAddress,
            SignUpSingleton.countryCode + SignUpSingleton.phoneNumber,
            SignUpSingleton.password,
            SignUpSingleton.confirmPassword,
            "${SignUpSingleton.birthYear}-${SignUpSingleton.birthMonth}-${SignUpSingleton.birthDay}",
            SignUpSingleton.gender
        ) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@signUp
            }

            if (result?.status == true) {
                bearerToken = result.user?.token ?: ""
                selectSportsFragment.mGetSports {
                    if (it)
                        view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                }
            }
        }
    }

    private fun signUpUserForSocial() {
        pBar(1)

        APIManager.signUpForSocial(
            SignUpSingleton.firstName,
            SignUpSingleton.lastName,
            emailAddress,
            "",
            "",
            SignUpSingleton.countryCode + SignUpSingleton.phoneNumber,
            "${SignUpSingleton.birthYear}-${SignUpSingleton.birthMonth}-${SignUpSingleton.birthDay}",
            SignUpSingleton.gender
        ) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@signUpForSocial
            }

            if (result?.status == true) {
                bearerToken = result.user?.token ?: ""
                selectSportsFragment.mGetSports {
                    if (it)
                        view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                }
            }
        }
    }

    private fun mSelectSports() {
        pBar(1)

        val auth = HashMap<String, String>()
        auth["Authorization"] = bearerToken

        val selectedSports = SignUpSingleton.selectedSports.joinToString { it }

        APIManager.selectSports(auth, selectedSports) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@selectSports
            }

            if (result?.status == true) {
                view_pager.setCurrentItem(view_pager.currentItem + 1, true)
            }
        }
    }
}