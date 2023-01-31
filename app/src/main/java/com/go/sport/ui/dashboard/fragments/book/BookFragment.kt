package com.go.sport.ui.dashboard.fragments.book

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.SyncInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getfacilities.GetFacilitiesData
import com.go.sport.model.getsports.GetSportsSport
import com.go.sport.network.APIManager
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.dashboard.fragments.book.adapter.BookAdapter
import com.go.sport.ui.dashboard.fragments.book.adapter.BookFilterAdapter
import com.go.sport.ui.dashboard.fragments.book.sports.BookNowSportsActivity
import com.go.sport.ui.dashboard.fragments.book.venuedetails.VenueDetailsActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Single
import kotlinx.android.synthetic.main.bottom_sheet_filter.*
import kotlinx.android.synthetic.main.fragment_book.view.*
import kotlinx.android.synthetic.main.fragment_book.view.iv_back
import kotlinx.android.synthetic.main.fragment_book.view.tv_filter
import kotlinx.android.synthetic.main.fragment_play.view.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class BookFragment : Fragment(),
    BookAdapter.BookClickListener {

    private lateinit var rootView: View
    private lateinit var bsd: BottomSheetDialog

    private val filterIds = ArrayList<Int>()
    private val facilities = arrayListOf<GetFacilitiesData>()
    private val sports = arrayListOf<GetSportsSport>()

    private var from = ""
    private var filterId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_book, container, false)
        from = arguments?.getString("from") ?: ""

        initViews()
        initListeners()
        mGetSports()

        return rootView
    }

    override fun onResume() {
        super.onResume()
        mGetFacilities()
    }

    private fun initViews() {
        if (from != "")
            rootView.iv_back.visibility = View.VISIBLE
        else
            rootView.iv_back.visibility = View.GONE

        initFilterBottomSheet()

        rootView.rv_book.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = BookAdapter(requireContext(), facilities, this@BookFragment)
        }
    }

    @SuppressLint("CheckResult")
    private fun initFilterBottomSheet() {
        bsd = BottomSheetDialog(requireContext())
        bsd.setContentView(R.layout.bottom_sheet_filter)
        bsd.rv_sports.apply {
            adapter = BookFilterAdapter(
                requireContext(),
                sports,
                filterIds,
                object : BookFilterAdapter.OnFiltersClickListener {
                    override fun onFilterItemClick(position: Int, getSportsSport: GetSportsSport) {
                        /*if (getSportsSport.id == 0) {
                            filterIds.clear()
                        } else {
                            if (!filterIds.contains(getSportsSport.id)) {
                                filterIds.add(getSportsSport.id)
                            } else {
                                filterIds.remove(getSportsSport.id)
                            }
                        }*/
                        sports.forEach { sport ->
                            if (sport != getSportsSport)
                                sport.isSelected = false
                        }
                        bsd.rv_sports.adapter?.notifyDataSetChanged()

                        getSportsSport.isSelected = !getSportsSport.isSelected
                        bsd.rv_sports.adapter?.notifyItemChanged(position)

                        filterId = if (getSportsSport.isSelected) {
                            getSportsSport.id.toString()
                        } else {
                            ""
                        }

                        bsd.rv_sports.adapter?.notifyDataSetChanged()
                    }
                }
            )
        }

        RxView.clicks(bsd.cont_apply).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (bsd.isShowing) {
                if (filterId.isEmpty()) {
                    (requireContext() as DashboardActivity).warningToast("Please select sport to continue")
                    return@subscribe
                }

                mFacilityFilters(
                    bsd.double_range_seekbar.currentMinValue.toString() + "," + bsd.double_range_seekbar.currentMaxValue.toString(),
                    bsd.et_location.text.toString()
                )
            }
        }

        RxView.clicks(bsd.tv_reset).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (bsd.isShowing) {
                filterId = "0"

                sports.forEach { sport ->
                    sport.isSelected = false
                }
                bsd.rv_sports.adapter?.notifyDataSetChanged()

                bsd.et_location.text?.clear()

                bsd.double_range_seekbar.currentMinValue = 0
                bsd.double_range_seekbar.currentMaxValue = 5000

                mGetFacilities()

                bsd.dismiss()
            }
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
        RxView.clicks(rootView.iv_back).subscribe {

        }

        RxView.clicks(rootView.tv_filter).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (!bsd.isShowing)
                bsd.show()
        }
    }

    private fun mGetSports() {
        (requireActivity() as BaseActivity).pBar(1)
        APIManager.getSports((requireActivity() as BaseActivity).returnUserAuthToken()) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)
            sports.clear()
            bsd.rv_sports.adapter?.notifyDataSetChanged()

            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@getSports
            }

            if (result?.status == true) {
                sports.addAll(result.sports)
                bsd.rv_sports.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun mFacilityFilters(price: String, facilityName: String) {
        val sportsIds = filterIds.joinToString { it.toString() }
        (requireActivity() as BaseActivity).pBar(1)
        bsd.dismiss()
        APIManager.facilityFilter(
            (requireActivity() as BaseActivity).returnUserAuthToken(),
            filterId,
            price,
            facilityName
        ) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)
            facilities.clear()
            rootView.rv_book.adapter?.notifyDataSetChanged()

            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@facilityFilter
            }

            if (result?.status == true) {
                result.facilities?.let { facilities.addAll(it) }

                if (facilities.isEmpty()) {
                    (requireActivity() as DashboardActivity).errorToast("No Venue(s) found for applied filters!")
                }

                rootView.rv_book.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun mGetFacilities() {
        (requireActivity() as BaseActivity).pBar(1)
        APIManager.getFacilities((requireActivity() as BaseActivity).returnUserAuthToken()) { result, error ->
            (requireContext() as BaseActivity).pBar(0)
            facilities.clear()
            rootView.rv_book.adapter?.notifyDataSetChanged()

            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@getFacilities
            }

            if (result?.status == true) {
                result.data?.let { facilities.addAll(it) }

                rootView.rv_book.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun openVenueDetail(position: Int) {
        Singleton.selectedFacilitiesId.clear()
        Singleton.selectedFacilitiesId.add(facilities[position].id.toString())
        Singleton.gameCost = facilities[position].pricing
        Singleton.isBookNowFromBookActivityClicked = true
        Singleton.selectedVenueId = facilities[position].id.toString()
        Singleton.selectedVenueName = facilities[position].name
        Singleton.selectedVenueAddress = facilities[position].address
        Singleton.sportsBook = facilities[position].facility_sports
        Singleton.VenueLocation = facilities[position].lat_lng
        Singleton.featuresList.addAll(facilities[position].feature)

        (requireActivity() as BaseActivity).startActivity(
            requireContext(),
            VenueDetailsActivity::class.java,
            false,
            1,
            bundleOf(
                Pair("id", facilities[position].id.toString()),
                Pair("address", facilities[position].address)
            )
        )
    }

    override fun onBookItemClick(position: Int) {
        openVenueDetail(position)
    }

    override fun onBookNowItemClick(position: Int) {

        Singleton.selectedFacilitiesId.clear()
        Singleton.selectedFacilitiesId.add(facilities[position].id.toString())
        Singleton.gameCost = facilities[position].pricing
        Singleton.isBookNowFromBookActivityClicked = true
        Singleton.selectedVenueId = facilities[position].id.toString()
        Singleton.selectedVenueName = facilities[position].name
        Singleton.selectedVenueAddress = facilities[position].address
        Singleton.sportsBook = facilities[position].facility_sports
        Singleton.featuresList.addAll(facilities[position].feature)
        Singleton.VenueLocation = facilities[position].lat_lng

        (requireContext() as BaseActivity).startActivity(
                requireContext(),
                BookNowSportsActivity::class.java,
                false,
                1,
                bundleOf(Pair("id", facilities[position].id.toString()), Pair("address", facilities[position].address)))


        //openVenueDetail(position)
    }
}
