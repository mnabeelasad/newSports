package com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.summary
//
//import android.annotation.SuppressLint
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.go.sport.R
//import com.go.sport.base.BaseActivity
//import com.go.sport.model.getfeatures.GetFeaturesData
//import com.go.sport.sharedpref.MySharedPreference
//import com.go.sport.singleton.Singleton
//import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.price.PricingFragment
//import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.summary.adapter.VenueFacilitiesAdapter
//import com.go.sport.ui.dashboard.playerprofile.PlayerProfileActivity
//import com.go.sport.ui.dashboard.summary.adapter.SummaryPlayersAdapter
//import kotlinx.android.synthetic.main.fragment_summary.view.*
//
//
//class SummaryFragment : Fragment(),
//    SummaryPlayersAdapter.SummaryPlayersClickListener {
////
////    private lateinit var rootView: View
////    private val amenities = arrayListOf<GetFeaturesData>()
////
////
////    private var pricingFragment = PricingFragment()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
////        // Inflate the layout for this fragment
////        rootView = inflater.inflate(R.layout.fragment_summary, container, false)
////
////        initViews()
////        setValues()
////
////        return rootView
//    }
//
////    private fun initViews() {
////        rootView.rv_amenities.apply {
////            adapter = VenueFacilitiesAdapter(requireActivity(), amenities)
////        }
////
////        /*rootView.rv_players.apply {
////            adapter = SummaryPlayersAdapter(requireActivity(), result.data, this@SummaryFragment)
////            layoutManager = LinearLayoutManager(requireActivity())
////        }*/
////    }
////
////    @SuppressLint("SetTextI18n")
////    private fun setValues() {
////        amenities.clear()
////
////        rootView.tv_user.text =
////            MySharedPreference(requireContext()).getUserObject()?.first_name + " " + MySharedPreference(
////                requireContext()
////            ).getUserObject()?.first_name
////        rootView.tv_title.text = Singleton.selectedSportsName
////        rootView.tv_location.text = Singleton.selectedVenueAddress
////        rootView.tv_time.text =
////            Singleton.selectedTimeSlotStartTime + "-" + Singleton.selectedTimeSlotEndTime
////        rootView.tv_duration.text = "Duration: ${Singleton.selectedTimeSlotDuration}"
////        rootView.tv_total_players.text = Singleton.totalPlayers
////        rootView.tv_date.text = Singleton.selectedDate
////        rootView.tv_confirmed_players.text = Singleton.confirmedPlayers
////        rootView.tv_price.text = "PKR ${Singleton.costPerPlay}"
////        rootView.tv_pitch.text = Singleton.selectedPitch
////        amenities.addAll(Singleton.selectedFacilities)
////
////        rootView.rv_amenities.adapter?.notifyDataSetChanged()
////    }
////
////    /*private fun getGameJoins() {
////        (requireActivity() as BaseActivity).pBar(1)
////        APIManager.gameJoins(
////            (requireActivity() as BaseActivity).returnUserAuthToken(),
////            myGame.id.toString()
////        ) { result, error ->
////            (requireActivity() as BaseActivity).pBar(0)
////
////            if (error != null) {
////                (requireActivity() as BaseActivity).mOnError(error)
////                return@gameJoins
////            }
////
////            if (result != null) {
////                rv_players.adapter = SummaryPlayersAdapter(
////                    requireContext(),
////                    result.data,
////                    object : SummaryPlayersAdapter.SummaryPlayersClickListener {
////                        override fun onSummaryPlayersItemClick(position: Int) {
////                            (requireActivity() as BaseActivity).startActivity(
////                                requireContext(),
////                                PlayerProfileActivity::class.java,
////                                false,
////                                1,
////                                bundleOf(Pair("user_id", result.data[position].userId))
////                            )
////                        }
////                    })
////            }
////        }
////    }
////
////    private fun getGameInvites() {
////        (requireActivity() as BaseActivity).pBar(1)
////        APIManager.gameInvites(
////            (requireActivity() as BaseActivity).returnUserAuthToken(),
////            myGame.id.toString()
////        ) { result, error ->
////            (requireActivity() as BaseActivity).pBar(0)
////
////            if (error != null) {
////                (requireActivity() as BaseActivity).mOnError(error)
////                return@gameInvites
////            }
////
////            if (result != null) {
////                rv_players.adapter = SummaryInvitesAdapter(
////                    requireContext(),
////                    result.data,
////                    object : SummaryInvitesAdapter.SummaryInvitesClickListener {
////                        override fun onSummaryInvitesItemClick(position: Int) {
////                            (requireActivity() as BaseActivity).startActivity(
////                                requireContext(),
////                                PlayerProfileActivity::class.java,
////                                false,
////                                1,
////                                bundleOf(Pair("user_id", result.data[position].userId))
////                            )
////                        }
////                    })
////            }
////        }
////    }*/
////
////    override fun onSummaryPlayersItemClick(position: Int) {
////        (requireActivity() as BaseActivity).startActivity(
////            requireContext(),
////            PlayerProfileActivity::class.java,
////            false,
////            1
////        )
////    }
//}