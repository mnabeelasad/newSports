package com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.price

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.singleton.Singleton
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.fragment_pricing.view.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class PricingFragment : Fragment() {

    private lateinit var rootView: View
    private var isCashSelected = false
    private var isWalletSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pricing, container, false)

        initListeners()


            if (Singleton.pricingType == "cash") {
                rootView.ic_buttons.visibility = View.VISIBLE
                rootView.action_buttons.visibility = View.GONE
                rootView.cont_cash_img.visibility = View.VISIBLE
                rootView.cont_wallet_img.visibility = View.GONE

            } else if (Singleton.pricingType == "wallet") {
                rootView.ic_buttons.visibility = View.VISIBLE
                rootView.cont_wallet_img.visibility = View.VISIBLE
                rootView.action_buttons.visibility = View.GONE
                rootView.cont_cash_img.visibility = View.GONE
            } else if (Singleton.pricingType == "both") {
                rootView.ic_buttons.visibility = View.GONE
                rootView.action_buttons.visibility = View.VISIBLE

            } else if (Singleton.pricingType == "card") {
                rootView.ic_buttons.visibility = View.VISIBLE
                rootView.cont_wallet_img.visibility = View.VISIBLE
                rootView.action_buttons.visibility = View.GONE
                rootView.cont_cash_img.visibility = View.GONE
            }

//        else if (Singleton.from == "book") {
//            if (Singleton.bookingSummaryPayment == "cash") {
//                rootView.ic_buttons.visibility = View.VISIBLE
//                rootView.action_buttons.visibility = View.GONE
//                rootView.cont_cash_img.visibility = View.VISIBLE
//                rootView.cont_wallet_img.visibility = View.GONE
//
//            } else if (Singleton.bookingSummaryPayment == "wallet") {
//                rootView.ic_buttons.visibility = View.VISIBLE
//                rootView.cont_wallet_img.visibility = View.VISIBLE
//                rootView.action_buttons.visibility = View.GONE
//                rootView.cont_cash_img.visibility = View.GONE
//            } else if (Singleton.bookingSummaryPayment == "both") {
//                rootView.ic_buttons.visibility = View.VISIBLE
//                rootView.action_buttons.visibility = View.GONE
//
//            } else if (Singleton.bookingSummaryPayment == "card") {
//                rootView.ic_buttons.visibility = View.VISIBLE
//                rootView.cont_wallet_img.visibility = View.VISIBLE
//                rootView.action_buttons.visibility = View.GONE
//                rootView.cont_cash_img.visibility = View.GONE
//            }
//        }
//        else if (Singleton.from == "location") {
//
//            if (Singleton.pricingType == "cash") {
//                rootView.cont_cash.background =
//                    ResourcesCompat.getDrawable(resources, R.drawable.bg_grad_purple, null)
//                rootView.tv_cash.setTextColor(
//                    ResourcesCompat.getColor(
//                        resources,
//                        R.color.white,
//                        null
//                    )
//                )
//            } else if (Singleton.pricingType == "wallet") {
//                rootView.cont_wallet.background =
//                    ResourcesCompat.getDrawable(resources, R.drawable.bg_grad_purple, null)
//                rootView.tv_wallet.setTextColor(
//                    ResourcesCompat.getColor(
//                        resources,
//                        R.color.white,
//                        null
//                    )
//                )
//            } else if (Singleton.pricingType == "both") {
//                rootView.cont_cash.background =
//                    ResourcesCompat.getDrawable(resources, R.drawable.bg_grad_purple, null)
//                rootView.tv_cash.setTextColor(
//                    ResourcesCompat.getColor(
//                        resources,
//                        R.color.white,
//                        null
//                    )
//                )
//                rootView.cont_wallet.background =
//                    ResourcesCompat.getDrawable(resources, R.drawable.bg_grad_purple, null)
//                rootView.tv_wallet.setTextColor(
//                    ResourcesCompat.getColor(
//                        resources,
//                        R.color.white,
//                        null
//                    )
//                )
//            }
//        }


        return rootView

    }

    fun getCost() {
        Singleton.costPerPlay = rootView.et_cost.text.toString()
//        if (isWalletSelected && isCashSelected)
//            Singleton.pricingType = "both"
//        else if (isWalletSelected)
//            Singleton.pricingType = "wallet"
         if (isCashSelected)
            Singleton.pricingType = "cash"
        else
            Singleton.pricingType = ""

        Log.d("PriceFragment", Singleton.costPerPlay)

    }


    fun getBookingCost() {
        Singleton.costPerPlay = rootView.et_cost.text.toString()
        Singleton.pricingType = Singleton.bookingSummaryPayment
        when (Singleton.bookingSummaryPayment) {
            "cash" -> {
                rootView.ic_buttons.visibility = View.VISIBLE
                rootView.action_buttons.visibility = View.GONE
                rootView.cont_cash_img.visibility = View.VISIBLE
                rootView.cont_wallet_img.visibility = View.GONE
            }
            "wallet" -> {
                rootView.ic_buttons.visibility = View.VISIBLE
                rootView.action_buttons.visibility = View.GONE
                rootView.cont_cash_img.visibility = View.GONE
                rootView.cont_wallet_img.visibility = View.VISIBLE
            }
            "both" -> {
                rootView.ic_buttons.visibility = View.GONE
                rootView.action_buttons.visibility = View.VISIBLE

            }
            "card" -> {
                rootView.ic_buttons.visibility = View.VISIBLE
                rootView.action_buttons.visibility = View.GONE
                rootView.cont_cash_img.visibility = View.GONE
                rootView.cont_wallet_img.visibility = View.VISIBLE
            }
        }
    }

    fun getConversionCost() {
        Singleton.costPerPlay = rootView.et_cost.text.toString()

        when (Singleton.bookingSummaryPayment) {
            "cash" -> {
                rootView.ic_buttons.visibility = View.VISIBLE
                rootView.cont_cash_img.visibility = View.VISIBLE
                rootView.cont_wallet_img.visibility = View.GONE
                rootView.action_buttons.visibility = View.GONE
            }
            "wallet" -> {
                rootView.ic_buttons.visibility = View.VISIBLE
                rootView.cont_wallet_img.visibility = View.VISIBLE
                rootView.action_buttons.visibility = View.GONE
                rootView.cont_cash_img.visibility = View.GONE
            }
            "both" -> {
                rootView.ic_buttons.visibility = View.VISIBLE
                rootView.action_buttons.visibility = View.GONE
            }
            "card" -> {
                rootView.ic_buttons.visibility = View.VISIBLE
                rootView.cont_wallet_img.visibility = View.VISIBLE
                rootView.action_buttons.visibility = View.GONE
                rootView.cont_cash_img.visibility = View.GONE
            }
        }
    }


    fun getCostString() {
        Singleton.costPerPlay = rootView.et_cost.text.toString()
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(rootView.cont_cash).subscribe {
            if (isCashSelected) {
                Singleton.pricingType = "cash"
                rootView.cont_cash.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.bg_grey, null)
                rootView.tv_cash.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.purple_dark_3,
                        null
                    )
                )
                isCashSelected = false
            } else {
                rootView.cont_cash.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.bg_grad_purple, null)
                rootView.tv_cash.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.white,
                        null
                    )
                )
                isCashSelected = true
            }
        }

        RxView.clicks(rootView.cont_wallet).subscribe {
            if (!isWalletSelected) {
                (requireActivity() as BaseActivity).disclaimer(
                    "Terms & Conditions\n\n" +
                            "In case of wallet payment, the users will be asked to pay the amount in order to confirm their participation. \n\n" +
                            "Payment collected from users will be credited to your GoPlay wallet upon completion of activity. These can be used for transactions that you make on GoPlay and cannot be redeemed by way of cash or bank transfers.\n\n" +
                            "In case there is a dispute with respect to payment or activity, GoPlay will investigate the matter and in doing so, reserves the right to take corrective measures to amicably resolve the dispute.\n\n"
                )
                {
                    rootView.cont_wallet.background =
                        ResourcesCompat.getDrawable(resources, R.drawable.bg_grad_purple, null)
                    rootView.tv_wallet.setTextColor(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.white,
                            null
                        )
                    )
                    isWalletSelected = true
                }
            } else {
                rootView.cont_wallet.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.bg_grey, null)
                rootView.tv_wallet.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.purple_dark_3,
                        null
                    )
                )
                isWalletSelected = false

            }
        }

    }
}