package com.go.sport.ui.dashboard.fragments.home.offers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.go.sport.R
import com.go.sport.model.getoffers.Offer
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.row_offer.view.*
import java.util.concurrent.TimeUnit

class OffersAdapter(
    private val c: Context,
    val offersList: ArrayList<Offer>,
    private val l: OffersClickListener
) :
    RecyclerView.Adapter<OffersAdapter.OffersAdapterViewHolder>() {
    class OffersAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var rv: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        rv = recyclerView
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OffersAdapterViewHolder {
        val view =
            OffersAdapterViewHolder(
                LayoutInflater.from(c).inflate(R.layout.row_offer, parent, false)
            )

//        view.itemView.layoutParams.width = (rv.measuredWidth / 4.8).toInt()

        return view
    }

    override fun onBindViewHolder(holder: OffersAdapterViewHolder, position: Int) {
        val data = offersList[position]

        holder.itemView.apply {

            tv_offer_text.text = data.text

            RxView.clicks(cont_root).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                l.onOffersItemClick(position)
            }
            RxView.clicks(facebook).throttleFirst(2,TimeUnit.SECONDS).subscribe {
                l.onFacebookClick(position)
            }
            RxView.clicks(google).throttleFirst(2,TimeUnit.SECONDS).subscribe {
                l.onGoogleClick(position)
            }
            RxView.clicks(whatsapp).throttleFirst(2,TimeUnit.SECONDS).subscribe {
                l.onWhatsappClick(position)
            }
        }
    }

    override fun getItemCount(): Int = offersList.size

    interface OffersClickListener {
        fun onOffersItemClick(position: Int)
        fun onFacebookClick(position: Int)
        fun onGoogleClick(position: Int)
        fun onWhatsappClick(position: Int)
    }
}
