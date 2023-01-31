package com.go.sport.ui.dashboard.fragments.wallet.transfermoney

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.FrameLayout
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.constants.Constants
import com.go.sport.model.getusers.GetUsersData
import com.go.sport.network.APIManager
import com.go.sport.ui.dashboard.fragments.wallet.transfermoney.adapter.SearchUserAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_transfer_money.*
import kotlinx.android.synthetic.main.bottom_sheet_transfer_money.*
import java.util.*
import java.util.concurrent.TimeUnit

class TransferMoneyActivity : BaseActivity(),
    SearchUserAdapter.SearchUserAdapterClickListener {

    private companion object {
        private const val TAG = "MyTransferMoney"
    }

    private val users = arrayListOf<GetUsersData>()
    private val copiedUsers = arrayListOf<GetUsersData>()

    private var receiverId = ""

    private lateinit var bsd: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_money)

        setFonts()
        initViews()
        initBottomSheet()
        initListeners()
        getAllUsers()
    }

    private fun setFonts() {
        setFont(Constants.REGULAR, til_search_user)
    }

    private fun initViews() {
        rv_users.adapter = SearchUserAdapter(this, users, this)
    }

    private fun initBottomSheet() {
        bsd = BottomSheetDialog(this)
        bsd.setContentView(R.layout.bottom_sheet_transfer_money)

        setFont(Constants.REGULAR, bsd.til_msg)
        setFont(Constants.REGULAR, bsd.til_amount)

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

        RxView.clicks(bsd.cont_request_to_transfer).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            transferMoney(receiverId)
        }

        et_search_user.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
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
        rv_users.adapter?.notifyDataSetChanged()
    }

    private fun getAllUsers() {
        pBar(1)
        APIManager.getUsers(returnUserAuthToken()) { response, error ->
            pBar(0)
            users.clear()

            if (error != null) {
                mOnError(error)
                return@getUsers
            }

            if (response?.status == true) {
                response.data?.let { users.addAll(it) }
                response.data?.let { copiedUsers.addAll(it) }
                rv_users.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun transferMoney(receiverId: String) {
        val amount = bsd.et_amount.text.toString()
        val reason = bsd.et_msg.text.toString()

        if (reason.isEmpty() || reason.isBlank()) {
            bsd.et_msg.requestFocus()
            warningToast("Invalid reason")
            return
        }

        if (amount.isEmpty() || amount.isBlank()) {
            bsd.et_amount.requestFocus()
            warningToast("Invalid amount")
            return
        }

        if (amount.toInt() == 0) {
            warningToast("Amount should be greater than 0")
            return
        }

        pBar(1)
        APIManager.transferAmount(
            returnUserAuthToken(),
            amount,
            reason,
            receiverId
        ) { response, error ->
            pBar(0)
            bsd.dismiss()

            users.forEach { user ->
                user.isClicked = false
            }
            rv_users.adapter?.notifyDataSetChanged()

            if (error != null) {
                mOnError(error)
                return@transferAmount
            }

            if (response?.status == true) {
                successToast(response.message)
            }
        }
    }

    override fun onSearchUserItemClick(searchUser: GetUsersData, position: Int) {
        for (i in users) {
            i.isClicked = false
            rv_users.adapter?.notifyDataSetChanged()
        }
        receiverId = searchUser.id.toString()
        searchUser.isClicked = true
        rv_users.adapter?.notifyItemChanged(position)

        if (!bsd.isShowing)
            bsd.show()
    }
}
