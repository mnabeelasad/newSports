package com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.venue.book

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getfacilities.GetFacilitiesData
import com.go.sport.network.APIManager
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.activities.FilterModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_book_venue.*
import kotlinx.android.synthetic.main.bottom_sheet_filter.*
import java.util.concurrent.TimeUnit

class BookVenueActivity : BaseActivity(),
    BookAdapter.BookClickListener {

    private lateinit var bsd: BottomSheetDialog
    private val filterIds = ArrayList<String>()
    private val facilities = arrayListOf<GetFacilitiesData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_venue)

        initViews()
        initFilterBottomSheet()
        initListeners()
        mGetFacilities()
    }

    private fun initViews() {
        rv_book.adapter = BookAdapter(this, facilities, this)
    }

    @SuppressLint("CheckResult")
    private fun initFilterBottomSheet() {
        bsd = BottomSheetDialog(this)
        bsd.setContentView(R.layout.bottom_sheet_filter)
        bsd.rv_sports.apply {
            adapter = BookFilterAdapter(
                this@BookVenueActivity, arrayListOf(
                    FilterModel("1", "Football"),
                    FilterModel("2", "Cricket"),
                    FilterModel("3", "Badminton"),
                    FilterModel("4", "Volleyball"),
                    FilterModel("5", "Tennis"),
                    FilterModel("6", "Hockey"),
                ),
                filterIds,
                object : BookFilterAdapter.OnFiltersClickListener {
                    override fun onFilterItemClick(position: Int, filter: FilterModel) {
                        if (!filterIds.contains(filter.id)) {
                            filterIds.add(filter.id)
                        } else {
                            filterIds.remove(filter.id)
                        }

                        // changing bottom sheet button and text color
                        if (filterIds.size > 0) {
                            bsd.cont_apply.background =
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.bg_grad_purple,
                                    null
                                )
                            bsd.tv_apply.setTextColor(
                                ContextCompat.getColor(
                                    this@BookVenueActivity,
                                    R.color.white
                                )
                            )
                        } else {
                            bsd.cont_apply.background =
                                ResourcesCompat.getDrawable(
                                    resources,
                                    R.drawable.bg_grey,
                                    null
                                )
                            bsd.tv_apply.setTextColor(
                                ContextCompat.getColor(
                                    this@BookVenueActivity,
                                    R.color.grey_2
                                )
                            )
                        }

                        bsd.rv_sports.adapter?.notifyDataSetChanged()
                    }
                }
            )
        }
        RxView.clicks(bsd.cont_apply).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (bsd.isShowing)
                bsd.dismiss()
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
    }

    private fun mGetFacilities() {
        pBar(1)
        APIManager.getFacilities(returnUserAuthToken()) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@getFacilities
            }

            if (result?.status == true) {
                facilities.clear()
                for(facility in result.data!!){
                    for(sport in facility.facility_sports)
                    {
                        if(sport.sportId == Singleton.selectedSportsId){
                            facilities.add(facility)
                            break;
                        }
                    }
                }
              //  result.data?.let { facilities.addAll(it) }


                rv_book.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onBookItemClick(position: Int) {
        Singleton.isBookNowFromBookActivityClicked = true
        Singleton.selectedVenueId = facilities[position].id.toString()
        Singleton.selectedVenueName = facilities[position].name
        Singleton.selectedVenueAddress = facilities[position].address
        Singleton.featuresList.addAll(facilities[position].feature)
        Singleton.VenueLocation = facilities[position].lat_lng
        Singleton.from = "venue"


        super.onBackPressed()
    }
}