package com.go.sport.ui.dashboard.fragments.home.requestfunds

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.widget.doOnTextChanged
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.fundrequest.received.ReceivedFundRequestsData
import com.go.sport.model.getusers.GetUsersData
import com.go.sport.network.APIManager
import com.go.sport.ui.dashboard.fragments.home.requestfunds.adapter.FundsRequestsAdapter
import com.go.sport.ui.dashboard.fragments.home.requestfunds.adapter.OnFundsRequests
import com.go.sport.ui.dashboard.fragments.home.requestfunds.adapter.OnRequestFunds
import com.go.sport.ui.dashboard.fragments.home.requestfunds.adapter.RequestFundsAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_request_funds.*
import kotlinx.android.synthetic.main.bsd_requestfunds.*
import java.util.*

class RequestFundsActivity : BaseActivity(),
    OnRequestFunds, OnFundsRequests {

    private lateinit var bsd: BottomSheetDialog

    private val users = arrayListOf<GetUsersData>()
    private val copiedUsers = arrayListOf<GetUsersData>()
    private val receivedFundsRequests = arrayListOf<ReceivedFundRequestsData>()

    private var userId = ""
    private var amount = ""
    private var desc = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_funds)

        initViews()
        initTabs()
        initListeners()
        initBottomSheet()
        mGetUsers()
    }

    private fun initViews() {
        rv_allusers.adapter = RequestFundsAdapter(this, users, this)
        rv_receivedrequests.adapter = FundsRequestsAdapter(this, receivedFundsRequests, this)
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position ?: 0 == 0) {
                    mGetUsers()
                } else {
                    mGetReceivedFundsRequests()
                }
            }
        })

        et_search.doOnTextChanged { text, _, _, count ->
            filter(text.toString())
        }
    }

    private fun initTabs() {
        tab_layout.addTab(tab_layout.newTab().setText("Request Fund"))
        tab_layout.addTab(tab_layout.newTab().setText("Received Requests"))
    }

    @SuppressLint("CheckResult")
    private fun initBottomSheet() {
        bsd = BottomSheetDialog(this)
        bsd.setContentView(R.layout.bsd_requestfunds)

        bsd.cont_request_to_transfer.setOnClickListener {
            mSendFundRequest()
        }

        bsd.setOnShowListener(DialogInterface.OnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
                ?: return@OnShowListener
            bottomSheet.background = null
        })
    }

    private fun filter(text: String) {
        users.clear()
        if (text.isEmpty()) {
            users.addAll(copiedUsers)
        } else {
            for (item in copiedUsers) {
                if (item.first_name.toLowerCase(Locale.getDefault())
                        .contains(text.toLowerCase(Locale.getDefault()))
                    ||
                    item.last_name.toLowerCase(Locale.getDefault())
                        .contains(text.toLowerCase(Locale.getDefault()))
                ) {
                    users.add(item)
                }
            }
        }
        rv_allusers.adapter?.notifyDataSetChanged()
    }

    private fun mGetUsers() {
        pBar(1)
        APIManager.getUsers(returnUserAuthToken()) { result, error ->
            pBar(0)

            rv_allusers.visibility = View.VISIBLE
            rv_receivedrequests.visibility = View.GONE
            cont_search.visibility = View.VISIBLE

            users.clear()
            copiedUsers.clear()
            rv_allusers.adapter?.notifyDataSetChanged()

            if (error != null) {
                mOnError(error)
                return@getUsers
            }

            if (result?.status == true) {
                result.data?.let { users.addAll(it) }
                result.data?.let { copiedUsers.addAll(it) }
                rv_allusers.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun mGetReceivedFundsRequests() {
        pBar(1)
        APIManager.receivedFundsRequests(returnUserAuthToken()) { result, error ->
            pBar(0)

            rv_allusers.visibility = View.GONE
            rv_receivedrequests.visibility = View.VISIBLE
            cont_search.visibility = View.GONE

            receivedFundsRequests.clear()
            rv_receivedrequests.adapter?.notifyDataSetChanged()

            if (error != null) {
                mOnError(error)
                return@receivedFundsRequests
            }

            if (result?.status == true) {
                result.data.let { receivedFundsRequests.addAll(it) }
                rv_receivedrequests.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun mSendFundRequest() {
        desc = bsd.et_msg.text.toString()
        amount = bsd.et_amount.text.toString()

        if (desc.isEmpty()) {
            warningToast("Invalid Reason")
            return
        }

        if (amount.isEmpty()) {
            warningToast("Invalid Amount")
            return
        }

        if (bsd.isShowing)
            bsd.dismiss()

        pBar(1)
        APIManager.sendFundRequest(
            returnUserAuthToken(),
            amount,
            desc,
            userId
        ) { result, error ->
            pBar(0)

            amount = ""
            desc = ""
            userId = ""

            if (error != null) {
                mOnError(error)
                return@sendFundRequest
            }

            if (result?.status == true) {
                successToast(result.message)
            }
        }
    }

    private fun mAcceptFundRequest(pos: Int) {
        pBar(1)
        APIManager.acceptFundRequest(
            returnUserAuthToken(),
            receivedFundsRequests[pos].amount,
            receivedFundsRequests[pos].description,
            receivedFundsRequests[pos].requestedBy,
            receivedFundsRequests[pos].id
        ) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@acceptFundRequest
            }

            if (result?.status == true) {
                successToast(result.message)

                receivedFundsRequests.removeAt(pos)
                rv_receivedrequests.adapter?.notifyItemRemoved(pos)
                rv_receivedrequests.adapter?.notifyItemRangeRemoved(pos, receivedFundsRequests.size)
            }
        }
    }

    private fun mDeclineFundRequest(pos: Int) {
        pBar(1)
        APIManager.declineFundRequest(
            returnUserAuthToken(),
            receivedFundsRequests[pos].id.toString()
        ) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@declineFundRequest
            }

            if (result?.status == true) {
                successToast(result.message)

                receivedFundsRequests.removeAt(pos)
                rv_receivedrequests.adapter?.notifyItemRemoved(pos)
                rv_receivedrequests.adapter?.notifyItemRangeRemoved(pos, receivedFundsRequests.size)
            }
        }
    }

    override fun onRequestClick(pos: Int) {
        userId = users[pos].id.toString()

        if (!bsd.isShowing)
            bsd.show()
    }

    override fun onAcceptClick(pos: Int) {
        mAcceptFundRequest(pos)
    }

    override fun onDeclineClick(pos: Int) {
        mDeclineFundRequest(pos)
    }
}