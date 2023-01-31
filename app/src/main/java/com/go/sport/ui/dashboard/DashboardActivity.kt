package com.go.sport.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.earnextramiles.app.models.eventbus.NotificationToken
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.invites.InvitesInvite
import com.go.sport.model.login.LoginDetail
import com.go.sport.model.login.LoginUser
import com.go.sport.network.APIManager
import com.go.sport.sharedpref.MySharedPreference
import com.go.sport.singleton.Singleton
import com.go.sport.singleton.Singleton.invitesInvite
import com.go.sport.ui.dashboard.accountsettings.AccountSettingsActivity
import com.go.sport.ui.dashboard.activities.ActivitiesActivity
import com.go.sport.ui.dashboard.contactus.ContactUsActivity
import com.go.sport.ui.dashboard.editprofile.EditProfileActivity
import com.go.sport.ui.dashboard.fragments.book.BookFragment
import com.go.sport.ui.dashboard.fragments.home.HomeFragment
import com.go.sport.ui.dashboard.fragments.home.hostactivity.HostActivityActivity
import com.go.sport.ui.dashboard.fragments.play.PlayFragment
import com.go.sport.ui.dashboard.fragments.wallet.WalletFragment
import com.go.sport.ui.dashboard.invites.InvitesActivity
import com.go.sport.ui.dashboard.mypastgames.MyPastGamesActivity
import com.go.sport.ui.dashboard.privacypolicy.PrivacyPolicyActivity
import com.go.sport.ui.dashboard.termsconditions.TermsAndConditionsActivity
import com.go.sport.ui.dashboard.transferfunds.TransferFundsActivity
import com.go.sport.ui.signin.SignInFirstActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.jakewharton.rxbinding2.view.RxView
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavLogger
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_invites.*
import kotlinx.android.synthetic.main.fragment_select_venue.view.*
import kotlinx.android.synthetic.main.menu_left_drawer.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit


class DashboardActivity : BaseActivity(),
    FragNavController.TransactionListener,
    FragNavController.RootFragmentListener {

    private companion object {
        private const val TAG = "DashboardActivity"
        const val HOME = FragNavController.TAB1
        const val PLAY = FragNavController.TAB2
        const val BOOK = FragNavController.TAB3
        const val WALLET = FragNavController.TAB4
    }

    private lateinit var toast: Toast

    lateinit var slide: SlidingRootNav

    private val invites = arrayListOf<InvitesInvite>()
    private val homeFragment = HomeFragment()
    private val playFragment = PlayFragment()
    private val bookFragment = BookFragment()
    private val walletFragment = WalletFragment()
    val fragNavController: FragNavController =
        FragNavController(supportFragmentManager, R.id.fragment_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        mGetFeatures()

        toast = infoToastOnBackPressed(getString(R.string.press_back_once_more_to_exit))

        initDrawer(savedInstanceState)
        initBottomBar(savedInstanceState)
        initBottomSheet("Are you sure to logout your account ?",
            "Logout",
            object : OnBottomSheetDialogClickListener {
                override fun onDismissButtonClick() {
                    areYouSureBottomSheet.dismiss()

                    MySharedPreference(this@DashboardActivity).clearAllPreferences()

                    startActivityFinishAll(
                        this@DashboardActivity,
                        SignInFirstActivity::class.java,
                        true,
                        -1
                    )
                }
            })

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(
            this
        ) {
            val token: String = it.token
            Log.i("FCM Token", token)
            mUpdateFcmToken(token)
        }
        /*myview.setOnClickListener {
            slide.closeMenu(true)
            myview.visibility = View.GONE
        }*/
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(n: NotificationToken) {
        Log.d("tokennew", n.token)
        mUpdateFcmToken(n.token)
    }


    override fun onResume() {
        super.onResume()
        getMyProfile()
    }

    private fun initDrawer(savedInstanceState: Bundle?) {
        slide = SlidingRootNavBuilder(this)
            .withMenuLayout(R.layout.menu_left_drawer)
            .withSavedState(savedInstanceState)
            .withMenuOpened(false)
            .withDragDistance(200)
            .withRootViewScale(0.8f)
            .withContentClickableWhenMenuOpened(false).inject()

//        invites.addAll(invites)
        getInvites(true)
        initDrawerListeners()
    }

    private fun getInvites(isRefresh: Boolean = false) {
        pBar(1)
        APIManager.allInvites(returnUserAuthToken()) { result, error ->
            pBar(0)
            if (isRefresh)
                invites.clear()

            if (error != null) {
                mOnError(error)
                return@allInvites
            }

            if (result?.status == true) {
                isRefresh
                invites.clear()
                invites.addAll(result.Invites)
                invites.reverse()
                if (invites.size != 0) {
                    for (dates in invites) {
                        if (dates.invite.accept_decline == "pending") {
                            invites_notification.visibility = View.VISIBLE
                        } else {
                            invites_notification.visibility = View.GONE
                        }
                    }
                } else {
                    invites_notification.visibility = View.GONE
                }

            }
        }
    }

    @SuppressLint("CheckResult")
    private fun initDrawerListeners() {
        RxView.clicks(cont_profile).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            slide.closeMenu(true)
            startActivity(
                this,
                EditProfileActivity::class.java,
                false,
                1
            )
        }
        RxView.clicks(cont_activities).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            slide.closeMenu(true)
            startActivity(
                this,
                ActivitiesActivity::class.java,
                false,
                1
            )
        }
        RxView.clicks(cont_invites).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            slide.closeMenu(true)
            startActivity(
                this,
                InvitesActivity::class.java,
                false,
                1
            )
        }

        RxView.clicks(cont_past_games).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            slide.closeMenu(true)
            startActivity(
                this,
                MyPastGamesActivity::class.java,
                false,
                1
            )
        }
        RxView.clicks(cont_account_settings).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            slide.closeMenu(true)
            startActivity(
                this,
                AccountSettingsActivity::class.java,
                false,
                1
            )
        }
        RxView.clicks(cont_transfer_funds).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            slide.closeMenu(true)
            startActivity(
                this,
                TransferFundsActivity::class.java,
                false,
                1
            )
        }
        RxView.clicks(cont_contact_us).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            slide.closeMenu(true)
            startActivity(
                this,
                ContactUsActivity::class.java,
                false,
                1
            )
        }
        RxView.clicks(cont_terms_and_condition).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            slide.closeMenu(true)
            startActivity(
                this,
                TermsAndConditionsActivity::class.java,
                false,
                1
            )
        }
        RxView.clicks(cont_privacy_policy).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            slide.closeMenu(true)
            startActivity(
                this,
                PrivacyPolicyActivity::class.java,
                false,
                1
            )
        }
        RxView.clicks(iv_logout).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            areYouSureBottomSheet.show()
        }
    }

    private fun initBottomBar(savedInstanceState: Bundle?) {
        fragNavController.apply {
            transactionListener = this@DashboardActivity
            rootFragmentListener = this@DashboardActivity
            createEager = true
            fragNavLogger = object : FragNavLogger {
                override fun error(message: String, throwable: Throwable) {
                    Log.e(TAG, message, throwable)
                }
            }

            defaultTransactionOptions = FragNavTransactionOptions.newBuilder().customAnimations(
                R.anim.slide_in_from_right,
                R.anim.slide_out_to_left,
                R.anim.slide_in_from_left,
                R.anim.slide_out_to_right
            ).build()

            fragmentHideStrategy = FragNavController.DETACH_ON_NAVIGATE_HIDE_ON_SWITCH

            navigationStrategy = UniqueTabHistoryStrategy(object : FragNavSwitchController {
                override fun switchTab(
                    index: Int,
                    transactionOptions: FragNavTransactionOptions?
                ) {
                    fragNavController.switchTab(index)
                    bottom_bar.setActiveItem(index)
                }
            })
        }

        fragNavController.initialize(HOME, savedInstanceState)

        val initial = savedInstanceState == null
        if (initial) {
            bottom_bar.setActiveItem(HOME)
        }

        bottom_bar.onItemSelected = { tabId ->
            if (slide.isMenuClosed)
                when (tabId) {
                    HOME -> {
                        fragNavController.switchTab(HOME)
                    }
                    PLAY -> {
                        fragNavController.switchTab(PLAY)
                    }
                    BOOK -> {
                        fragNavController.switchTab(BOOK)
                    }
                    WALLET -> {
                        fragNavController.switchTab(WALLET)
                    }
                }
        }

        bottom_bar.onItemReselected = { fragNavController.clearStack() }
    }

    private fun getMyProfile() {
        pBar(1)
        APIManager.myProfile(returnUserAuthToken()) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@myProfile
            }

            if (result?.status == true) {
                val circularProgressDrawable = CircularProgressDrawable(this)
                circularProgressDrawable.centerRadius = 25f
                circularProgressDrawable.start()

                if (result.data.detail != null) {
                    Glide.with(this).load(result.data.detail.profile_image)
                        .placeholder(circularProgressDrawable).into(iv_image)
                    tv_user.text = "${result.data.first_name} ${result.data.last_name}"
                    tv_gender.text = result.data.detail.gender.capitalize()
                    tv_age.text = getAgeFromDOB(
                        result.data.detail.date_of_birth.split("-")[0].toInt(),
                        result.data.detail.date_of_birth.split("-")[1].toInt(),
                        result.data.detail.date_of_birth.split("-")[2].toInt()
                    )

                    var user = MySharedPreference(this).getUserObject()
                    MySharedPreference(this).saveUserObject(
                        LoginUser(
                            result.data.created_at,
                            "",
                            LoginDetail(
                                result.data.detail.country_id,
                                result.data.detail.created_at,
                                result.data.detail.date_of_birth,
                                result.data.detail.gender,
                                result.data.detail.id,
                                result.data.detail.nick_name,
                                result.data.detail.profile_image,
                                result.data.detail.updated_at,
                                result.data.detail.user_id
                            ),
                            result.data.email,
                            "",
                            result.data.first_name,
                            result.data.id,
                            result.data.last_name,
                            user!!.token,
                            result.data.updated_at
                        )
                    )
                }
            }
        }
    }

    private fun mUpdateFcmToken(token: String) {

        APIManager.updateFcmToken(returnUserAuthToken(), token) { result, error ->
            if (error != null) {
                mOnError(error)
                return@updateFcmToken
            }

            if (result != null) {
                Log.d(TAG, "mUpdateFcmToken: $token")
            }
        }
    }

    override fun onFragmentTransaction(
        fragment: Fragment?,
        transactionType: FragNavController.TransactionType
    ) {
        supportActionBar?.setDisplayHomeAsUpEnabled(fragNavController.isRootFragment.not())
    }

    override fun onTabTransaction(fragment: Fragment?, index: Int) {
        supportActionBar?.setDisplayHomeAsUpEnabled(fragNavController.isRootFragment.not())
    }

    override val numberOfRootFragments: Int = 4

    override fun getRootFragment(index: Int): Fragment {
        when (index) {
            HOME -> return homeFragment
            PLAY -> return playFragment
            BOOK -> return bookFragment
            WALLET -> return walletFragment
        }
        throw IllegalStateException("Need to send an index that we know")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onBackPressed() {
        if (fragNavController.popFragment().not()) {
            if (fragNavController.isRootFragment)
                if (toast.view?.isShown == true) {
                    super.onBackPressed()
                } else {
                    toast.show()
                }
        }
    }


    private fun mGetFeatures() {
        pBar(1)
        APIManager.getFeatures(returnUserAuthToken()) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@getFeatures
            }

            if (result?.status == true) {
                Singleton.features.clear()
                Singleton.features.addAll(result.data)
            }
        }
    }
}