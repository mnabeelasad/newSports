package com.go.sport.ui.dashboard.fragments.wallet

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.constants.Constants
import com.go.sport.model.wallet.history.WalletHistoryData
import com.go.sport.network.APIManager
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.dashboard.fragments.wallet.adapter.TransactionsAdapter
import com.go.sport.ui.dashboard.fragments.wallet.transfermoney.TransferMoneyActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.bottom_sheet_transfer_money.*
import kotlinx.android.synthetic.main.fragment_wallet.view.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class WalletFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var bsd: BottomSheetDialog

    private val walletHistory = arrayListOf<WalletHistoryData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_wallet, container, false)

        initViews()
        initBottomSheet()
        initListeners()

        return rootView
    }

    override fun onResume() {
        super.onResume()

        getWalletAmount()
        getWalletHistory()
    }

    private fun initBottomSheet() {
        bsd = BottomSheetDialog(requireContext())
        bsd.setContentView(R.layout.bottom_sheet_transfer_money)
        bsd.til_msg.visibility = View.GONE
        bsd.tv_request_to_transfer.text = "Recharge Wallet"

        (requireActivity() as DashboardActivity).setFont(Constants.REGULAR, bsd.til_msg)
        (requireActivity() as DashboardActivity).setFont(Constants.REGULAR, bsd.til_amount)

        bsd.setOnShowListener(DialogInterface.OnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
                ?: return@OnShowListener
            bottomSheet.background = null
        })
    }

    private fun initViews() {
        rootView.tv_no_transactions.visibility = View.GONE
        rootView.rv_transactions.adapter = TransactionsAdapter(requireContext(), walletHistory)
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(rootView.cont_recharge).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (!bsd.isShowing)
                bsd.show()
        }

        RxView.clicks(bsd.cont_request_to_transfer).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            rechargeAmount()
        }

        RxView.clicks(rootView.cont_transfer_money).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (requireActivity() as BaseActivity).startActivity(
                requireContext(),
                TransferMoneyActivity::class.java,
                false,
                1
            )
        }
    }

    private fun getWalletAmount() {
        (requireActivity() as DashboardActivity).pBar(1)
        APIManager.walletAmount((requireActivity() as DashboardActivity).returnUserAuthToken()) { response, error ->
            (requireActivity() as DashboardActivity).pBar(0)

            if (error != null) {
                (requireActivity() as DashboardActivity).mOnError(error)
                return@walletAmount
            }

            if (response?.status == true) {
                rootView.tv_balance.text = "PKR " + response.data
            }
        }
    }

    private fun getWalletHistory() {
        (requireActivity() as DashboardActivity).pBar(1)
        APIManager.walletHistory((requireActivity() as DashboardActivity).returnUserAuthToken()) { response, error ->
            (context as DashboardActivity).pBar(0)
            walletHistory.clear()

            if (error != null) {
                (context as DashboardActivity).mOnError(error)
                rootView.tv_no_transactions.visibility = View.VISIBLE
                return@walletHistory
            }

            if (response?.status == true) {
                walletHistory.addAll(response.data)
                walletHistory.reverse()
                rootView.rv_transactions.adapter?.notifyDataSetChanged()

                if (walletHistory.isEmpty())
                    rootView.tv_no_transactions.visibility = View.VISIBLE
                else
                    rootView.tv_no_transactions.visibility = View.GONE
            }
        }
    }

    private fun rechargeAmount() {
        val amount = bsd.et_amount.text.toString()

        if (amount.isEmpty() || amount.isBlank()) {
            bsd.et_amount.requestFocus()
            (requireActivity() as DashboardActivity).warningToast("Invalid amount")
            return
        }

        if (amount.toInt() == 0) {
            (requireActivity() as DashboardActivity).warningToast("Amount should be greater than 0")
            return
        }

        (requireActivity() as DashboardActivity).pBar(1)
        APIManager.rechargeWallet(
            (requireActivity() as DashboardActivity).returnUserAuthToken(),
            amount
        ) { response, error ->
            (requireActivity() as DashboardActivity).pBar(0)
            bsd.dismiss()

            if (error != null) {
                (requireActivity() as DashboardActivity).mOnError(error)
                return@rechargeWallet
            }

            if (response?.status == true) {
                (requireActivity() as DashboardActivity).successToast(response.message)
                rootView.tv_balance.text = "PKR ${rootView.tv_balance.text.split(" ")[1].toInt() + amount.toInt()}"
            }
        }
    }
}