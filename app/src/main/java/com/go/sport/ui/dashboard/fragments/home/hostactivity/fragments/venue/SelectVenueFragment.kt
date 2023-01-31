package com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.venue

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.facebook.internal.Mutable
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getfeatures.GetFeaturesData
import com.go.sport.network.APIManager
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.fragments.home.hostactivity.HostActivityActivity
import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.venue.adapter.OnVenueClickListener
import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.venue.adapter.VenueFacilitiesAdapter
import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.venue.book.BookVenueActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_host_activity.*
import kotlinx.android.synthetic.main.fragment_select_venue.view.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class SelectVenueFragment : Fragment() {

    private lateinit var rootView: View

    private val features = arrayListOf<GetFeaturesData>()
    private val selectedFeatures = arrayListOf<GetFeaturesData>()
    private val selectedFeaturesId = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_select_venue, container, false)

        initRecyclerView()
        initListeners()
        mGetFeatures()

        if (Singleton.selectedVenueAddress != "" && Singleton.from == "location") {
            rootView.tv_loc.text = Singleton.selectedVenueAddress
        }

        return rootView
    }

    fun setVenues() {
        if (Singleton.selectedFacilitiesId.size > 0) {
            for (i in 0 until Singleton.selectedFacilitiesId.size) {
                for (j in 0 until features.size)
                    if (Singleton.selectedFacilitiesId[i] == features[j].id) {
                        features[j].isSelected = true
                    }
            }
            rootView.rv_amenities.adapter!!.notifyDataSetChanged()
        }
    }

    fun getAdditionalInfo() {
        Singleton.additionalInfo = if (rootView.et_additional_info.text.toString().isNotEmpty()) {
            rootView.et_additional_info.text.toString()
        } else "none"
    }

    fun getLocation() {
        val location = rootView.tv_loc.text.toString()

        Singleton.selectedVenueAddress =
            if (!location.isNullOrEmpty() && location != "Search Location") {
                location
            } else {
                ""
            }
    }

    private fun initRecyclerView() {
        val venueAdapter = VenueFacilitiesAdapter(requireActivity(), features)
        rootView.rv_amenities.apply {
            adapter = venueAdapter
        }
        venueAdapter.setListener(object : OnVenueClickListener {
            override fun onVenueclicked(
                position: Int,
                feature: GetFeaturesData
            ) {
                if (feature.isSelected) {
                    if (selectedFeaturesId.contains(feature.id))
                        selectedFeaturesId.remove(feature.id)
                    if (selectedFeatures.contains(feature))
                        selectedFeatures.remove(feature)
                } else {
                    if (!selectedFeaturesId.contains(feature.id))
                        selectedFeaturesId.add(feature.id)
                    if (!selectedFeatures.contains(feature))
                        selectedFeatures.add(feature)
                }
                Singleton.selectedFacilitiesId.clear()
                Singleton.selectedFacilities.clear()
                Singleton.selectedFacilitiesId.addAll(selectedFeaturesId)
                Singleton.selectedFacilities.addAll(selectedFeatures)

                features[position].isSelected = !features[position].isSelected
                venueAdapter.notifyDataSetChanged()
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(rootView.cont_book_venue).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (selectedFeatures.size > 0) {

                (requireActivity() as HostActivityActivity).warningToast("Facility selection is only available in Location")

            } else {
                Singleton.from = "venue"
                getAdditionalInfo()
                (requireActivity() as BaseActivity).startActivity(
                    requireContext(),
                    BookVenueActivity::class.java,
                    false,
                    1
                )
            }
        }

        RxView.clicks(rootView.cont_search_location).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            Singleton.from = "location"
            val fields: List<Place.Field> = listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
            val intent: Intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            ).setCountries(listOf("AE", "PAK"))
                .build(requireContext())
            startActivityForResult(intent, 99)


        }
    }

    private fun mGetFeatures() {
        (requireActivity() as HostActivityActivity).pBar(1)
        APIManager.getFeatures((requireActivity() as HostActivityActivity).returnUserAuthToken()) { result, error ->
            (requireActivity() as HostActivityActivity).pBar(0)

            if (error != null) {
                (requireActivity() as HostActivityActivity).mOnError(error)
                return@getFeatures
            }

            if (result?.status == true) {
                selectedFeaturesId.clear()
                features.clear()
                features.addAll(result.data)

                rootView.rv_amenities.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 99) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)

                    val geoCoder = Geocoder(requireContext(), Locale.getDefault())

                    if (place.address != null)
                        rootView.tv_loc.text = place.name

                    if (place.latLng != null)
                        Singleton.latLngFromLocation =
                            "${place.latLng!!.latitude},${place.latLng!!.longitude}"

                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status = Autocomplete.getStatusFromIntent(data!!)
                    (requireActivity() as BaseActivity).errorToast("Error: " + status.statusMessage)
                }
                Activity.RESULT_CANCELED -> {
                    // The myBookingUser canceled the operation.
                }
            }
        }
    }
}