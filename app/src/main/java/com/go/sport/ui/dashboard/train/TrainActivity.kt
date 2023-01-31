package com.go.sport.ui.dashboard.train

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.academies.AcademiesAcadmy
import com.go.sport.model.getsports.GetSportsSport
import com.go.sport.network.APIManager
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.train.adapter.AcademyAdapter
import com.go.sport.ui.dashboard.train.adapter.BookFilterAdapter
import com.go.sport.ui.dashboard.train.adapter.IndividualAdapter
import com.go.sport.ui.dashboard.train.trainsingleitem.TrainItemActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_train.*
import kotlinx.android.synthetic.main.bottom_sheet_filter.*
import java.util.concurrent.TimeUnit


class TrainActivity : BaseActivity(),
    AcademyAdapter.AcademyAdapterClickListener,
    IndividualAdapter.IndividualAdapterClickListener {

    private val bottomSheetFilterIds = ArrayList<Int>()
    private val academies = ArrayList<AcademiesAcadmy>()
    private val sports = arrayListOf<GetSportsSport>()

    private lateinit var bsd: BottomSheetDialog

    private var filterId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train)

        initTabs()
        initViews()
        initFilterBottomSheet()
        initListeners()
        mGetSports()
        mGetAcademies()
    }

    private fun initTabs() {
        tab_layout.addTab(tab_layout.newTab().setText("Academies"))
        tab_layout.addTab(tab_layout.newTab().setText("Individuals"))
    }

    private fun initViews() {
        rv_academies.apply {
            layoutManager =
                LinearLayoutManager(this@TrainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = AcademyAdapter(this@TrainActivity, academies, this@TrainActivity)
        }

        rv_individuals.apply {
            layoutManager =
                LinearLayoutManager(this@TrainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = AcademyAdapter(this@TrainActivity, academies,object : AcademyAdapter.AcademyAdapterClickListener{
                override fun onAcademyItemClick(academy: AcademiesAcadmy) {
                    Singleton.academy = academy
                    startActivity(this@TrainActivity, TrainItemActivity::class.java, false, 1)
                }
            })
        }
    }

    @SuppressLint("CheckResult")
    private fun initFilterBottomSheet() {
        bsd = BottomSheetDialog(this)
        bsd.setContentView(R.layout.bottom_sheet_filter)
        bsd.rv_sports.apply {
            adapter = BookFilterAdapter(
                this@TrainActivity,
                sports,
                object : BookFilterAdapter.OnFiltersClickListener {
                    override fun onFilterItemClick(position: Int, getSportsSport: GetSportsSport) {
                        if (!bottomSheetFilterIds.contains(getSportsSport.id)) {
                            bottomSheetFilterIds.add(getSportsSport.id)
                        } else {
                            bottomSheetFilterIds.remove(getSportsSport.id)
                        }

                        sports.forEach { sport ->
                            if (sport != getSportsSport)
                                sport.isSelected = false
                        }
                        bsd.rv_sports.adapter?.notifyDataSetChanged()

                        getSportsSport.isSelected = !getSportsSport.isSelected
                        bsd.rv_sports.adapter?.notifyItemChanged(position)

                        filterId = if (getSportsSport.isSelected) {
                            getSportsSport.id.toString()
                        } else {
                            ""
                        }

                        bsd.rv_sports.adapter?.notifyDataSetChanged()
                    }
                }
            )
        }
        RxView.clicks(bsd.cont_apply).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (bsd.isShowing) {
                if (filterId.isEmpty()) {
                    warningToast("Please select sport to continue")
                    return@subscribe
                }

//                if (bsd.et_location.text.toString().isEmpty()) {
//                    warningToast("Invalid location")
//                    return@subscribe
//                }
//
//                mAcademiesFilter(
//                    bsd.double_range_seekbar.currentMinValue.toString() + "," + bsd.double_range_seekbar.currentMaxValue.toString(),
//                    bsd.et_location.text.toString()
//                )

                bsd.dismiss()
            }
        }
        bsd.tv_reset.setOnClickListener {
            filterId = ""
            bsd.et_location.setText("")
            bottomSheetFilterIds.clear()
            bsd.rv_sports.adapter?.notifyDataSetChanged()
            bsd.dismiss()
            mGetAcademies()
        }

        bsd.setOnShowListener(DialogInterface.OnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
                ?: return@OnShowListener
            bottomSheet.background = null
        })
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(tv_filter).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (!bsd.isShowing)
                bsd.show()
        }

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position ?: 0 == 0) {
                    rv_individuals.visibility = View.GONE
                    rv_academies.visibility = View.VISIBLE
                } else {
                    rv_academies.visibility = View.GONE
                    rv_individuals.visibility = View.VISIBLE
                }
            }
        })

        /*RxView.clicks(cont_chat).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            startActivity(this, ChatsActivity::class.java, false, 1)
        }*/
    }

    private fun mGetSports() {
        pBar(1)
        APIManager.getSports(returnUserAuthToken()) { result, error ->
            pBar(0)
            sports.clear()
            bsd.rv_sports.adapter?.notifyDataSetChanged()

            if (error != null) {
                mOnError(error)
                return@getSports
            }

            if (result?.status == true) {
                sports.addAll(result.sports)
                bsd.rv_sports.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun mGetAcademies() {
        pBar(1)
        APIManager.getAcademies(returnUserAuthToken()) { result, error ->
            pBar(0)
            academies.clear()
            rv_academies.adapter?.notifyDataSetChanged()

            if (error != null) {
                mOnError(error)
                return@getAcademies
            }

            if (result?.status == true) {
                result.acadmies?.let { academies.addAll(it) }
                rv_academies.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun mAcademiesFilter(price: String, location: String) {
        pBar(1)
        APIManager.academyFilter(
            returnUserAuthToken(),
            filterId,
            price,
            location
        ) { result, error ->
            pBar(0)
            academies.clear()
            rv_academies.adapter?.notifyDataSetChanged()

            if (error != null) {
                mOnError(error)
                return@academyFilter
            }

            if (result?.status == true) {
                result.academy?.let { academies.addAll(it) }
                rv_academies.adapter?.notifyDataSetChanged()

                if (academies.isEmpty())
                    errorToast("No Train(s) found for applied filters!")
            }
        }
    }

    override fun onAcademyItemClick(academy: AcademiesAcadmy) {
        Singleton.academy = academy
        startActivity(this, TrainItemActivity::class.java, false, 1)
    }

    override fun onIndividualClick(position: Int) {
        startActivity(this, TrainItemActivity::class.java, false, 1)
    }
}