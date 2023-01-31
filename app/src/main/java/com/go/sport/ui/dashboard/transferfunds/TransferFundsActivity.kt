package com.go.sport.ui.dashboard.transferfunds

import android.annotation.SuppressLint
import android.os.Bundle
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.fundtransferhistory.FundTransferHistoryData
import com.go.sport.network.APIManager
import com.go.sport.ui.dashboard.transferfunds.adapter.TransferFundsAdapter
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_transfer_funds.*

class TransferFundsActivity : BaseActivity() {

    private var startDate = ""
    private var endDate = ""

    private val funds = arrayListOf<FundTransferHistoryData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_funds)
        listeners()



    }

    @SuppressLint("CheckResult")
    private fun listeners() {
        rv_transferfunds.adapter = TransferFundsAdapter(this, funds)

        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }
        tv_start_date.text =   getCurrentDateminusseven("yyyy-MM-dd")
        tv_end_date.text =   getCurrentDate("yyyy-MM-dd")
        startDate =  getCurrentDateminusseven("yyyy-MM-dd")
        endDate =getCurrentDate("yyyy-MM-dd")

        fundTransfers()
        RxView.clicks(cont_start_date).subscribe {
            showDatePicker(this, tv_start_date, "yyyy-MM-dd") {
                    date ->
                if (date != null) {
                    startDate = date
                    fundTransfers()
                }
            }




        }

        RxView.clicks(cont_end_date).subscribe {
            showDatePicker(this, tv_end_date, "yyyy-MM-dd") { date ->
                if (date != null) {
                    endDate = date
                    fundTransfers()
                }
            }
        }
    }

    private fun fundTransfers() {
        if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
            pBar(1)
            APIManager.fundTransferHistory(
                returnUserAuthToken(),
                startDate,
                endDate
            ) { result, error ->
                pBar(0)
                funds.clear()
                rv_transferfunds.adapter?.notifyDataSetChanged()

                if (error != null) {
                    mOnError(error)
                    return@fundTransferHistory
                }

                if (result != null) {
                    tv_sent.text = "PKR ${result.sent}"
                    tv_received.text = "PKR ${result.recieve}"

                    funds.addAll(result.data)
                    funds.reverse()
                    rv_transferfunds.adapter?.notifyDataSetChanged()
                }
            }
        } else {
            warningToast("Please select start date and end date to continue")
        }
    }
}