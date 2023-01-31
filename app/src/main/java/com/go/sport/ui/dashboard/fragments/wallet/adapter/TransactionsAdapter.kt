package com.go.sport.ui.dashboard.fragments.wallet.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.model.wallet.history.WalletHistoryData
import kotlinx.android.synthetic.main.row_transaction.view.*

class TransactionsAdapter(
    private val c: Context,
    private val walletHistory: ArrayList<WalletHistoryData>,
) :
    RecyclerView.Adapter<TransactionsAdapter.CategoriesAdapterViewHolder>() {
    class CategoriesAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesAdapterViewHolder {
        return CategoriesAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_transaction, parent, false)
        )
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: CategoriesAdapterViewHolder, position: Int) {
        val history = walletHistory[position]

        holder.itemView.apply {
            tv_user.text = history.reference_number
            tv_reason.text = history.long_description.capitalize()
            tv_amount.text = "PKR " + history.amount
            tv_date.text = history.date_appended
            tv_time.text = history.time_appended
            tv_status.text = history.type.capitalize()
        }
    }

    override fun getItemCount(): Int = walletHistory.size
}