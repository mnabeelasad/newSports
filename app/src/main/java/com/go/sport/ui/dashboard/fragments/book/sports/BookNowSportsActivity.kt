package com.go.sport.ui.dashboard.fragments.book.sports

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.os.bundleOf
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getfacilities.GetFacilitiesFeatures
import com.go.sport.model.getsports.GetSportsSport
import com.go.sport.network.APIManager
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.fragments.book.hostactivity.BookNowHostActivity
import com.go.sport.ui.dashboard.fragments.book.sports.adapter.BookNowSportsAdapter
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_book_now_host.*
import kotlinx.android.synthetic.main.activity_book_now_sports.*
import kotlinx.android.synthetic.main.activity_book_now_sports.cont_next
import kotlinx.android.synthetic.main.activity_book_now_sports.iv_back
import kotlinx.android.synthetic.main.fragment_select_sports_for_hosting.view.*
import java.util.concurrent.TimeUnit

class BookNowSportsActivity : BaseActivity(), BookNowSportsAdapter.OnSelectSportsClickListener {

    private val sports = arrayListOf<GetSportsSport>()

    private var id = ""
    private var address = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_now_sports)

        id = intent?.extras?.getString("id") ?: ""
        address = intent?.extras?.getString("address") ?: ""


        initViews()
        initListener()
//        mGetSports()
    }

    @SuppressLint("CheckResult")
    private fun initListener() {
        RxView.clicks(iv_back).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(cont_next).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (Singleton.selectedSportsName.isNullOrEmpty()) {
                warningToast("Please select sport to continue")
                return@subscribe
            }

            startActivity(
                this,
                BookNowHostActivity::class.java,
                true,
                1, bundleOf(Pair("id", id), Pair("address", address))
            )
        }
    }

    private fun initViews() {
        rv_sports.adapter = BookNowSportsAdapter(this, Singleton.sportsBook, this)
    }

//    private fun mGetSports() {
//        pBar(1)
//        APIManager.getSports(returnUserAuthToken()) { result, error ->
//            pBar(0)
//
//            if (error != null) {
//                mOnError(error)
//                return@getSports
//            }
//
//            if (result?.status == true) {
//                sports.clear()
//                sports.addAll(result.sports)
//
//                    rv_sports.adapter?.notifyDataSetChanged()
//                }
//        }
//    }

    override fun onSelectSportsItemClick(position: Int, selectSport: GetFacilitiesFeatures) {
        for (i in 0 until sports.size)
            if (i != position)
                sports[i].isSelected = false
        rv_sports.adapter?.notifyDataSetChanged()

        if (!selectSport.sport.isSelected) {
            selectSport.sport.isSelected = true
            Singleton.selectedSportsId = selectSport.id.toString()
            Singleton.selectedSportsName = selectSport.sport.name
            Singleton.sportsImage = selectSport.sport.image
            Singleton.pitchCourt = selectSport.sport.name
        } else {
            selectSport.sport.isSelected = false
            Singleton.selectedSportsId = ""
            Singleton.selectedSportsName = ""
        }

        rv_sports.adapter?.notifyItemChanged(position)
    }
}