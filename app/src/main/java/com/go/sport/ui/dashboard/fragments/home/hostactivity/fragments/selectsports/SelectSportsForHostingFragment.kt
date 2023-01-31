package com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.selectsports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.go.sport.R
import com.go.sport.model.getsports.GetSportsSport
import com.go.sport.network.APIManager
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.fragments.home.hostactivity.HostActivityActivity
import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.selectsports.adapter.SelectSportsAdapter
import kotlinx.android.synthetic.main.fragment_select_sports_for_hosting.view.*

/**
 * A simple [Fragment] subclass.
 */
class SelectSportsForHostingFragment : Fragment(), SelectSportsAdapter.OnSelectSportsClickListener {

     lateinit var rootView: View

     val sports = arrayListOf<GetSportsSport>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_select_sports_for_hosting, container, false)

        initViews()
        mGetSports()

        if(Singleton.selectedSportsId!=""){
            for(i in 0 until sports.size){
                if(sports[i].id.toString()==Singleton.selectedSportsId){
                    sports[i].isSelected = true
                }
            }
        }

        rootView.rv_sports.adapter!!.notifyDataSetChanged()
        return rootView
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initViews() {
        rootView.rv_sports.adapter = SelectSportsAdapter(requireContext(), sports, this)
    }

    private fun mGetSports() {
        (requireActivity() as HostActivityActivity).pBar(1)
        APIManager.getSports((requireActivity() as HostActivityActivity).returnUserAuthToken()) { result, error ->
            (requireActivity() as HostActivityActivity).pBar(0)

            if (error != null) {
                (requireActivity() as HostActivityActivity).mOnError(error)
                return@getSports
            }

            if (result?.status == true) {
                sports.clear()
                sports.addAll(result.sports)

                rootView.rv_sports.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onSelectSportsItemClick(position: Int, selectSport: GetSportsSport) {
        for (i in 0 until sports.size)
            if (i != position)
                sports[i].isSelected = false
        rootView.rv_sports.adapter?.notifyDataSetChanged()

        if (!selectSport.isSelected) {
            selectSport.isSelected = true
            Singleton.selectedSportsId = selectSport.id.toString()
            Singleton.selectedSportsName = selectSport.name
            Singleton.sportsImage = selectSport.image
            Singleton.pitchCourt = selectSport.name
        } else {
            selectSport.isSelected = false
            Singleton.selectedSportsId = ""
            Singleton.selectedSportsName = ""
        }

        rootView.rv_sports.adapter?.notifyItemChanged(position)
    }
}