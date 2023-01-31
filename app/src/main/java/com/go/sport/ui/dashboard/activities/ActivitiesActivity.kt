package com.go.sport.ui.dashboard.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.ui.dashboard.activities.adapter.FilterAdapter
import com.go.sport.ui.dashboard.activities.adapter.MyActivitiesAdapter
import com.go.sport.ui.dashboard.activities.adapter.PendingActivitiesAdapter
import com.google.android.material.tabs.TabLayout
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_activities.*

class ActivitiesActivity : BaseActivity(), FilterAdapter.OnFiltersClickListener {

//    private var from = ""
    private val filterIds = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities)
        init()
        initListeners()
    }

    private fun init() {
//        from = intent?.extras?.getString("from") ?: ""

        initTabLayout()
        initRecycler()

        /*if (from == "my") {
            tab_layout.selectTab(tab_layout.getTabAt(0), true)
            rv_my_activities.visibility = View.VISIBLE
            rv_pending_activities.visibility = View.GONE
        } else if (from == "pending") {
            tab_layout.selectTab(tab_layout.getTabAt(1), true)
            rv_pending_activities.visibility = View.VISIBLE
            rv_my_activities.visibility = View.GONE
        }*/
    }

    private fun initTabLayout() {
        tab_layout.addTab(tab_layout.newTab().setText("Hosted"))
        tab_layout.addTab(tab_layout.newTab().setText("Pending"))
        tab_layout.addTab(tab_layout.newTab().setText("Confirmed"))
    }

    private fun initRecycler() {
        rv_filter.apply {
            layoutManager =
                LinearLayoutManager(this@ActivitiesActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = FilterAdapter(
                this@ActivitiesActivity,
                arrayListOf(
                    FilterModel("0", "All"),
                    FilterModel("1", "Football"),
                    FilterModel("2", "Cricket"),
                    FilterModel("3", "Badminton"),
                    FilterModel("4", "Volleyball")
                ),
                filterIds,
                this@ActivitiesActivity
            )
        }

        rv_my_activities.apply {
            layoutManager = LinearLayoutManager(this@ActivitiesActivity)
            adapter = MyActivitiesAdapter(this@ActivitiesActivity)
        }

        rv_pending_activities.apply {
            layoutManager = LinearLayoutManager(this@ActivitiesActivity)
            adapter = PendingActivitiesAdapter(this@ActivitiesActivity)
        }
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position ?: 0 == 0) {
                    rv_my_activities.visibility = View.VISIBLE
                    rv_pending_activities.visibility = View.GONE
                } else {
                    rv_pending_activities.visibility = View.VISIBLE
                    rv_my_activities.visibility = View.GONE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    override fun onFilterItemClick(position: Int, filter: FilterModel) {
        if (filter.id == "0") {
            filterIds.clear()
        } else {
            if (!filterIds.contains(filter.id)) {
                filterIds.add(filter.id)
            } else {
                filterIds.remove(filter.id)
            }
        }
        rv_filter.adapter?.notifyDataSetChanged()
    }
}

data class FilterModel(val id: String, val title: String, var isSelected: Boolean = false)