package com.go.sport.ui.dashboard.fragments.home.hostactivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.viewpager.widget.ViewPager
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.constants.Constants
import com.go.sport.network.APIManager
import com.go.sport.newui.NewBookingSummaryScreen
import com.go.sport.singleton.Singleton
import com.go.sport.singleton.Singleton.selectedTimeSlotStartTime
import com.go.sport.ui.dashboard.DashboardActivity

import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.attributes.SelectAttributesFragment
import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.date.SelectDateFragment
import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.newsummaryfragment.NewSummaryFragment
import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.price.PricingFragment
import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.selectsports.SelectSportsForHostingFragment

import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.venue.SelectVenueFragment
import com.go.sport.ui.signup.adapter.ViewPagerAdapter
import com.jakewharton.rxbinding2.view.RxView
import com.kofigyan.stateprogressbar.StateProgressBar
import io.reactivex.Single
import io.reactivex.SingleConverter
import kotlinx.android.synthetic.main.activity_host_activity.*
import kotlinx.android.synthetic.main.activity_host_activity.tv_title
import kotlinx.android.synthetic.main.fragment_new_summary_screen.*
import kotlinx.android.synthetic.main.fragment_new_summary_screen.view.*
import kotlinx.android.synthetic.main.fragment_pricing.*
import kotlinx.android.synthetic.main.fragment_select_date.*
import kotlinx.android.synthetic.main.fragment_select_sports_for_hosting.view.*
import kotlinx.android.synthetic.main.fragment_summary.*
import kotlinx.android.synthetic.main.fragment_summary.tv_price
import java.util.concurrent.TimeUnit

class HostActivityActivity : BaseActivity() {


    private companion object {
        private const val SPORTS = 0
        private const val VENUE = 1
        private const val DATE = 2
        private const val ATTRIBUTES = 3
        private const val PRICING = 4
        private const val SUMMARY = 5
    }

    private val selectSportsForHostingFragment = SelectSportsForHostingFragment()
    private val selectVenueFragment = SelectVenueFragment()
    private val selectDateFragment = SelectDateFragment()
    private val selectAttributesFragment = SelectAttributesFragment()
    private val pricingFragment = PricingFragment()
    private val summaryFragment = NewSummaryFragment()
    private var from = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_activity)


        init()
        initViews()
        initListeners()
    }

    override fun onResume() {
        super.onResume()

        if (view_pager.currentItem == 3)
            return

        if (Singleton.isBookNowFromBookActivityClicked && Singleton.from == "venue")
            view_pager.setCurrentItem(view_pager.currentItem + 1, true)

        if (Singleton.from == "book") {
            view_pager.setCurrentItem(view_pager.currentItem + 3, true)
        }
    }

    private fun init() {
        from = intent?.extras?.getString("from") ?: ""
    }

    private fun initViews() {
        state_progress_bar.setMaxStateNumber(StateProgressBar.StateNumber.SIX)
        state_progress_bar.setStateDescriptionData(
            arrayOf(
                "Sports",
                "Venue",
                "Time",
                "Player",
                "Pricing",
                "Summary"
            )
        )
        state_progress_bar.setStateDescriptionTypeface(Constants.REGULAR)
        state_progress_bar.stateDescriptionSize = 12f

        if (from == "book") {
//            state_progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
            state_progress_bar.visibility = View.GONE
        } else {
            state_progress_bar.visibility = View.VISIBLE
        }

        setUpViewPager()
    }

    private fun setUpViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, 5)
        viewPagerAdapter.addFragment(SPORTS, selectSportsForHostingFragment)
        viewPagerAdapter.addFragment(VENUE, selectVenueFragment)
        viewPagerAdapter.addFragment(DATE, selectDateFragment)
        viewPagerAdapter.addFragment(ATTRIBUTES, selectAttributesFragment)
        viewPagerAdapter.addFragment(PRICING, pricingFragment)
        viewPagerAdapter.addFragment(SUMMARY, summaryFragment)

        view_pager.adapter = viewPagerAdapter
        view_pager.offscreenPageLimit = 0
        view_pager.setScrollDuration(500)
        view_pager.setPagingEnabled(false)

        if (from == "book")
            view_pager.setCurrentItem(3, true)
        else
            view_pager.setCurrentItem(0, true)
    }

    private fun popViewPagerFragment() {
        when {
            Singleton.from == "book" && view_pager.currentItem == 3 -> startActivityFinishAll(
                this,
                DashboardActivity::class.java,
                true,
                -1
            )
            view_pager.currentItem > 0 -> {
                Handler().postDelayed({
                    view_pager.animate()
                    view_pager.setCurrentItem(
                        view_pager.currentItem - 1,
                        true
                    )
                }, 40)
            }
            else -> finish(this, -1)
        }
    }

    override fun onBackPressed() {
        if (Singleton.isFinish == true) {
            startActivityFinishAll(
                this,
                DashboardActivity::class.java,
                true,
                -1
            )
        } else {
            popViewPagerFragment()
        }
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            if (Singleton.isFinish == true) {
                startActivityFinishAll(
                    this,
                    DashboardActivity::class.java,
                    true,
                    -1
                )
            } else {
                popViewPagerFragment()
            }

//            super.onBackPressed()
        }

        RxView.clicks(cont_next).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            // check if viewpager currentItem is the 2nd last fragment and from is not empty then open BookNowSummaryActivity

            if (view_pager.currentItem == PRICING && Singleton.from == "book") {
                pricingFragment.getCost()
                summaryFragment.player_summary_cost.text = Singleton.costPerPlay
                summaryFragment.payment_summary_type.text = Singleton.pricingType
                if (Singleton.costPerPlay == "") {
                    warningToast("Please enter cost per player to continue")
                    return@subscribe
                }

                closeKeyboard()
                view_pager.setCurrentItem(view_pager.currentItem + 1, true)


            } else if (view_pager.currentItem < SUMMARY) {
                when (view_pager.currentItem) {
                    SPORTS -> {
                        if (Singleton.selectedSportsId == "") {
                            warningToast("Please select any sport to continue")
                            return@subscribe
                        }

                        view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                    }
                    VENUE -> {
                        selectVenueFragment.getLocation()
                        selectVenueFragment.getAdditionalInfo()
                        if (Singleton.selectedVenueAddress.isEmpty()) {
                            warningToast("Please select location to continue")
                            return@subscribe
                        }
                        if (Singleton.selectedFacilitiesId.isEmpty()) {
                            warningToast("Please select facility(s) to continue")
                            return@subscribe
                        }

                        view_pager.setCurrentItem(view_pager.currentItem + 1, true)

                    }
                    DATE -> {
                        selectDateFragment.getStartTime()
                        selectDateFragment.getEndTime()


                        if (Singleton.from == "venue") {
                            selectDateFragment.getCost()

                            if (Singleton.selectedPitchId == "") {
                                warningToast("Please select any pitch to continue")
                                return@subscribe
                            }

                            if (Singleton.selectedDate == "") {
                                warningToast("Please select any date to continue")
                                return@subscribe
                            }


                            if (Singleton.selectedTimeSlots.isEmpty()) {
                                warningToast("Please select any time slot to continue")
                                return@subscribe
                            }
                        } else if (Singleton.from == "location") {
                            Singleton.selectedVenueId = "0"
                            selectDateFragment.getPitch()
                            selectDateFragment.getPrice()
                            if (Singleton.selectedDate == "") {
                                warningToast("Please select date to continue")
                                return@subscribe
                            }

                            if (selectedTimeSlotStartTime == "") {
                                warningToast("Please select start time to continue")
                                return@subscribe
                            }

                            if (Singleton.timeStampStartTime >= Singleton.timeStampEndTime) {
                                warningToast("Please Select Proper Time Slots")
                                return@subscribe
                            }



                            if (Singleton.selectedTimeSlotEndTime == "") {
                                warningToast("Please select end time to continue")
                                return@subscribe
                            }

                            if (Singleton.selectedPitch == "") {
                                warningToast("Please select pitch to continue")
                                return@subscribe
                            }

                        }

                        view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                    }
                    ATTRIBUTES -> {

                        selectAttributesFragment.getAgeRangeAndPlayersCount()
//                        selectDateFragment.getPitch()
                        if (Singleton.skillLevel == "") {
                            warningToast("Please select any skill to continue")
                            return@subscribe
                        }

                        if (Singleton.totalPlayers == "") {
                            warningToast("Please enter total players to continue")
                            return@subscribe
                        }

                        if (Singleton.confirmedPlayers == "") {
                            warningToast("Please enter confirmed players to continue")
                            return@subscribe
                        }

                        if ((Singleton.confirmedPlayers.toInt() != Singleton.totalPlayers.toInt())
                            and (Singleton.confirmedPlayers.toInt() > Singleton.totalPlayers.toInt())
                        ) {
                            warningToast("Please confirm players must be less than or equal to total players")
                            return@subscribe
                        }

                        if (Singleton.gender == "") {
                            warningToast("Please select any gender to continue")
                            return@subscribe
                        }

                        if (Singleton.eventType == "") {
                            warningToast("Please select any event type to continue")
                            return@subscribe
                        }

                        if (Singleton.from == "location") {
                            if (Singleton.eventType == "invites") {
                                selectDateFragment.getPitch()
                            }
                            view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                        } else if (Singleton.from == "venue") {
                            selectDateFragment.getCost()
                            view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                        } else
                            view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                    }
                    PRICING -> {
                        if (Singleton.from == "venue") {

                            pricingFragment.getCost()
                            summaryFragment.game_summary_cost.text = Singleton.gameCost
                            summaryFragment.player_summary_cost.text = Singleton.costPerPlay
                            summaryFragment.payment_summary_type.text = Singleton.pricingType
                                Singleton.bookingSummaryPayment
                        } else if (Singleton.from == "location") {
                            pricingFragment.getCost()
                            selectDateFragment.getPitch()
                            summaryFragment.player_summary_cost.text = Singleton.costPerPlay
                            summaryFragment.payment_summary_type.text = Singleton.pricingType

                            if (Singleton.pricingType == "") {
                                warningToast("Please select payment type to continue")
                                return@subscribe
                            }
                        }

                        Log.d("PriceFragment", Singleton.costPerPlay)

                        if (Singleton.costPerPlay == "") {
                            warningToast("Please enter cost per player to continue")
                            return@subscribe
                        }

                        closeKeyboard()
                        view_pager.setCurrentItem(view_pager.currentItem + 1, true)
                    }
                }
            } else {
                super.onBackPressed()
            }
        }

        view_pager.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    setProgressOfStepper(position + 1)
                    changeViews(position)
                }

                override fun onPageSelected(position: Int) {
                }
            })
    }

    private fun setProgressOfStepper(count: Int) {
        when (count) {
            1 -> state_progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE)
            2 -> state_progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO)
            3 -> state_progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE)
            4 -> state_progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
            5 -> state_progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE)
            6 -> state_progress_bar.setCurrentStateNumber(StateProgressBar.StateNumber.SIX)
        }
    }

    private fun changeViews(position: Int) {
        if (position == SUMMARY) {

            tv_title.text = "Summary"
            cont_next.visibility = View.GONE
            summary_cont_btns.visibility = View.VISIBLE
        } else {

            cont_main.background = ContextCompat.getDrawable(this, R.drawable.bg_upload_picture)
            tv_title.text = "Host an Activity"
            tv_next.text = "Next"
            cont_next.visibility = View.VISIBLE
        }
        if (position == SPORTS) {
            if (Singleton.selectedSportsId != "") {
                for (i in 0 until selectSportsForHostingFragment.sports.size) {
                    if (selectSportsForHostingFragment.sports[i].id.toString() == Singleton.selectedSportsId) {
                        selectSportsForHostingFragment.sports[i].isSelected = true
                    }
                }
            }
            selectSportsForHostingFragment.rootView.rv_sports.adapter!!.notifyDataSetChanged()
        }
        if (position == VENUE) {
            if (Singleton.from == "location")
                selectVenueFragment.setVenues()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Singleton.old_id = -1
        Singleton.isFinish = false
        Singleton.selectedSportsId = ""
        Singleton.selectedSportsName = ""
        Singleton.selectedSportsId = ""
        Singleton.selectedVenueId = ""
        Singleton.selectedVenueName = ""
        Singleton.selectedVenueAddress = ""
        Singleton.selectedPitchId = ""
        Singleton.selectedPitch = ""
        Singleton.selectedDate = ""
        Singleton.gameCost = ""
        Singleton.selectedFacilities = arrayListOf()
        Singleton.selectedFacilitiesId = arrayListOf()
        Singleton.selectedTimeSlots = arrayListOf()
        Singleton.additionalInfo = "none"
        Singleton.skillLevel = ""
        Singleton.totalPlayers = ""
        Singleton.confirmedPlayers = ""
        Singleton.ageMin = ""
        Singleton.ageMax = ""
        Singleton.gender = ""
        Singleton.eventType = ""
        Singleton.pricingType = ""
        Singleton.startTimingIn = ""
        Singleton.startTimingOut = ""
        Singleton.from = "location"
        Singleton.isBookNowFromBookActivityClicked = false
        Singleton.isInvitesActivityOpened = false
        Singleton.userInvites.clear()
        Singleton.groupInvites.clear()
        selectedTimeSlotStartTime = ""
        Singleton.selectedTimeSlotEndTime = ""
        Singleton.VenueLocation = ""
        Singleton.selectedVenueId = ""
    }


}