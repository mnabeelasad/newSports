package com.go.sport.ui.dashboard.mypastgames

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.mygames.Data
import com.go.sport.network.APIManager
import com.go.sport.newui.NewSummaryScreen
import com.go.sport.newui.ViewBookingSummaryActivity
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.fragments.home.adapter.MybookingAdapter
import com.go.sport.ui.dashboard.fragments.home.mygames.adapter.MyGamesAdapter
import com.google.android.material.tabs.TabLayout
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_my_games.*
import kotlinx.android.synthetic.main.activity_my_past_games.*

class MyPastGamesActivity : BaseActivity(),
    MyGamesAdapter.MyGamesClickListener,
    MybookingAdapter.MyBookingClickListener{

    private val myGames = arrayListOf<Data>()
    private val myBookings = arrayListOf<Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_past_games)


        refreshBooking()
        refresh()
        initListeners()
        initTabLayout()
        initViews()
    }

    private fun refresh() {
        swipe_past_refresh.setOnRefreshListener {
            page =1
            nextPage = "1"
            myGames(page, true)
            swipeRefreshLayout.setRefreshing(false)
        }
    }


    private fun refreshBooking() {
        swipe_past_booking_refresh.setOnRefreshListener {
            bookingPage =1
            bookingNextPage = "1"
            myBookings(bookingPage, true)
            swipeBookingRefresh.setRefreshing(false)
        }
    }

    private fun initViews() {
        rv_my_past_games.apply {
            adapter =
                MyGamesAdapter(
                    this@MyPastGamesActivity,
                    myGames,
                    this@MyPastGamesActivity
                )
        }

        rv_my_past_bookings.apply {
            layoutManager =
            LinearLayoutManager(this@MyPastGamesActivity)
        adapter = MybookingAdapter(
            this@MyPastGamesActivity,
            myBookings,
            this@MyPastGamesActivity
        )
    }
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(ic_back).subscribe {
            super.onBackPressed()
        }

        rv_my_past_games.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if(!nextPage.isNullOrBlank()){
                        page += 1
                        myGames(page)
                    }
                }
            }
        })
        rv_my_past_bookings.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if(!bookingNextPage.isNullOrBlank()){
                        bookingPage += 1
                        myBookings(bookingPage)
                    }
                }
            }
        })

        past_games_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position ?: 0 == 0) {
                    page =1
                    myGames.clear()
                    myGames(page)
                    rv_my_past_games.visibility = View.VISIBLE
                    rv_my_past_bookings.visibility = View.GONE
                } else {
                    bookingPage =1
                    myBookings.clear()
                    myBookings(bookingPage)
                    rv_my_past_games.visibility = View.GONE
                    rv_my_past_bookings.visibility = View.VISIBLE
                }
            }


            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun initTabLayout() {
        past_games_tab_layout.addTab(past_games_tab_layout.newTab().setText("My Past Games"))
        past_games_tab_layout.addTab(past_games_tab_layout.newTab().setText("My Past Bookings"))
    }

    var nextPage = "1"
    var page = 1
    private fun myGames(page : Int, isRefresh : Boolean= false) {
        pBar(1)
        APIManager.pastGames(returnUserAuthToken(), page) { result, error ->
            pBar(0)
            if(isRefresh)
                myGames.clear()

            if (error != null) {
                mOnError(error)
                return@pastGames
            }

            if (result?.status == true) {
                myGames.addAll(result.myGames.data)
                nextPage = result.myGames.nextPageUrl?:""
                rv_my_past_games.adapter?.notifyDataSetChanged()

                if (myGames.size == 0)
                    errorToast("No past games found!")
            }
        }
    }

    var bookingNextPage = "1"
    var bookingPage = 1

    private fun myBookings(bookingPage : Int, isBookingRefresh: Boolean = false) {
        pBar(1)
        APIManager.pastBookings(returnUserAuthToken(), bookingPage) { result, error ->
            pBar(0)
            if(isBookingRefresh)
                myBookings.clear()
            if (error != null) {
                mOnError(error)
                return@pastBookings
            }

            if (result?.status == true) {
                myBookings.addAll(result.myBookings.data)
                bookingNextPage = result.myBookings.nextPageUrl?:""

                rv_my_past_bookings.adapter?.notifyDataSetChanged()

                if (myBookings.size == 0)
                    errorToast("No bookings found!")
            }
        }
    }

    override fun onMyGamesItemClick(position: Int) {
        Singleton.homeClickedMyGamesModel = myGames[position]
        Singleton.isPastGame = true
        startActivity(this, NewSummaryScreen::class.java, false, 1)
    }

    override fun onMyBookingItemClick(position: Int) {
        Singleton.homeClickedMyGamesModel = myBookings[position]
        Singleton.isPastGame = true
        startActivity(this, ViewBookingSummaryActivity::class.java, false, 1)
    }
}