package com.go.sport.ui.dashboard.transferfunds.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.model.fundtransferhistory.FundTransferHistoryData
import kotlinx.android.synthetic.main.row_transferfunds.view.*

class TransferFundsAdapter(
    private val c: Context,
    private val funds: ArrayList<FundTransferHistoryData>
) :
    RecyclerView.Adapter<TransferFundsAdapter.TransferFundsAdapterViewHolder>() {
    class TransferFundsAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var rv: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        rv = recyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransferFundsAdapterViewHolder {
        return TransferFundsAdapterViewHolder(
            LayoutInflater.from(c).inflate(R.layout.row_transferfunds, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TransferFundsAdapterViewHolder, position: Int) {
        val fund = funds[position]

        holder.itemView.apply {
            tv_user.text = fund.shortDescription
            tv_desc.text = fund.longDescription
            tv_amount.text = "PKR ${fund.amount}"
            tv_date.text = fund.dateAppended
            tv_time.text = fund.TimeAppended
            tv_status.text = if (fund.type == "debit") "Transferred" else "Received"
        }
    }

    override fun getItemCount(): Int = funds.size

    interface InvitesClickListener {
        fun onInvitesItemClick(position: Int)
        fun onInvitesJoinGameClick(position: Int)
    }
}
