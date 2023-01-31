package com.go.sport.ui.signup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getsports.GetSportsSport
import com.go.sport.network.APIManager
import com.go.sport.ui.signup.SignUpActivity
import com.go.sport.ui.signup.SignUpSingleton
import com.go.sport.ui.signup.adapter.SelectSportsAdapter
import kotlinx.android.synthetic.main.fragment_select_sports.view.*

/**
 * A simple [Fragment] subclass.
 */
class SelectSportsFragment : Fragment(),
    SelectSportsAdapter.OnSelectSportsClickListener {

    private lateinit var rootView: View
    private var selectedSports = arrayListOf<String>()
    private val sports = arrayListOf<GetSportsSport>()

    /*private var isFootballSelected = false
    private var isCricketSelected = false
    private var isBadmintonSelected = false
    private var isTennisSelected = false
    private var isTableTennisSelected = false
    private var isVolleyBallSelected = false
    private var isBasketBallSelected = false*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_select_sports, container, false)

//        makeTicksVisibilityGone()
//        initListeners()
        initRecyclerView()

        return rootView
    }

    private fun initRecyclerView() {
        rootView.rv_sports.adapter =
            SelectSportsAdapter(
                requireContext(),
                sports,
                this
            )
    }

    fun initSelectSportsValidation(): Boolean {
        if (selectedSports.size == 0) {
            (requireActivity() as BaseActivity).warningToast("Please select at least one to continue")
            return false
        }

        SignUpSingleton.selectedSports.addAll(selectedSports)

        return true
    }

    fun mGetSports(isSuccessful: (Boolean) -> Unit) {
        if ((requireActivity() as SignUpActivity).bearerToken.isNotEmpty()) {
            (requireActivity() as SignUpActivity).pBar(1)

            val auth = HashMap<String, String>()
            auth["Authorization"] = (requireActivity() as SignUpActivity).bearerToken

            APIManager.getSports(auth) { result, error ->
                (requireActivity() as SignUpActivity).pBar(0)

                if (error != null) {
                    isSuccessful(false)
                    (requireActivity() as SignUpActivity).mOnError(error)
                    return@getSports
                }

                if (result?.status == true) {
                    isSuccessful(true)
                    sports.clear()
                    sports.addAll(result.sports)

                    rootView.rv_sports.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onSelectSportsItemClick(position: Int, selectSport: GetSportsSport) {
        selectSport.isSelected = !selectSport.isSelected

        if (selectSport.isSelected)
            selectedSports.add(selectSport.id.toString())
        else
            selectedSports.remove(selectSport.id.toString())

        rootView.rv_sports.adapter?.notifyItemChanged(position)
    }
}