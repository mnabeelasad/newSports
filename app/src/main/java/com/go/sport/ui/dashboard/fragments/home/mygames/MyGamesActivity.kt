package com.go.sport.ui.dashboard.fragments.home.mygames

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.mygames.Data
import com.go.sport.network.APIManager
import com.go.sport.newui.NewBookingSummaryScreen
import com.go.sport.newui.NewSummaryScreen
import com.go.sport.newui.ViewBookingSummaryActivity
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.fragments.home.adapter.MybookingAdapter
import com.go.sport.ui.dashboard.fragments.home.mygames.adapter.MyGamesAdapter
import com.google.android.material.tabs.TabLayout
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_my_games.*


class MyGamesActivity : BaseActivity(),
    MyGamesAdapter.MyGamesClickListener,
    MybookingAdapter.MyBookingClickListener {

    private val myGames = arrayListOf<Data>()
    private val myBookings = arrayListOf<Data>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_games)

        refreshBooking()
        refresh()
        initViews()
        initListeners()
        initTabLayout()
    }

     fun refresh(){

         swipeRefreshLayout.setOnRefreshListener {
             page =1
             nextPage = "1"
             myGames(page, true)
             swipeRefreshLayout.setRefreshing(false)
         }
    }
    fun refreshBooking(){

        swipeBookingRefresh.setOnRefreshListener {
            bookingPage =1
            bookingNextPage = "1"
            myBookings(bookingPage, true)
            swipeBookingRefresh.setRefreshing(false)
        }
    }

    private fun initViews() {
        rv_my_games.apply {
            adapter =
                MyGamesAdapter(
                    this@MyGamesActivity,
                    myGames,
                    this@MyGamesActivity
                )
        }

        rv_my_bookings.apply {
            layoutManager =
                LinearLayoutManager(this@MyGamesActivity)
            adapter = MybookingAdapter(
                this@MyGamesActivity,
                myBookings,
                this@MyGamesActivity
            )
        }
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        rv_my_games.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        rv_my_bookings.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position ?: 0 == 0) {
                    page =1
                    myGames.clear()
                    myGames(page)
                    rv_my_games.visibility = View.VISIBLE
                    rv_my_bookings.visibility = View.GONE
                } else {
                    bookingPage =1
                    myBookings.clear()
                    myBookings(bookingPage)
                    rv_my_games.visibility = View.GONE
                    rv_my_bookings.visibility = View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
    //swipeto refresh
    //

    private fun initTabLayout() {
        tab_layout.addTab(tab_layout.newTab().setText("My Games"))
        tab_layout.addTab(tab_layout.newTab().setText("My Bookings"))
    }

    var nextPage = "1"
    var page = 1
    private fun myGames(page : Int, isRefresh : Boolean= false) {
        pBar(1)
        APIManager.getGamesOnly(returnUserAuthToken(), page) { result, error ->
            pBar(0)
            if(isRefresh)
            myGames.clear()

            if (error != null) {
                mOnError(error)
                return@getGamesOnly
            }

            if (result?.status == true) {
                myGames.addAll(result.myGames.data)
                nextPage = result.myGames.nextPageUrl?:""
                rv_my_games.adapter?.notifyDataSetChanged()

                if (myGames.size == 0)
                    errorToast("No games found!")
            }
        }
    }

    var bookingNextPage = "1"
    var bookingPage = 1

    private fun myBookings(bookingPage : Int, isBookingRefresh: Boolean = false) {
        pBar(1)
        APIManager.getMyBookings(returnUserAuthToken(), bookingPage) { result, error ->
            pBar(0)
            if(isBookingRefresh)
                myBookings.clear()
            if (error != null) {
                mOnError(error)
                return@getMyBookings
            }

            if (result?.status == true) {
                myBookings.addAll(result.myBookings.data)
                bookingNextPage = result.myBookings.nextPageUrl?:""

                rv_my_bookings.adapter?.notifyDataSetChanged()

                if (myBookings.size == 0)
                    errorToast("No bookings found!")
            }
        }
    }

    override fun onMyGamesItemClick(position: Int) {
        Singleton.homeClickedMyGamesModel = myGames[position]
        startActivity(this, NewSummaryScreen::class.java, false, 1)
    }

    override fun onMyBookingItemClick(position: Int) {
      //  Singleton.myBookingsModel = myBookings[position]
        Singleton.homeClickedMyGamesModel = myBookings[position]
        startActivity(this, ViewBookingSummaryActivity::class.java, false, 1)
    }

}