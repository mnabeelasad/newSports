package com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.attributes

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.fragments.home.hostactivity.community.SelectCommunityGroupActivity
import com.jakewharton.rxbinding2.view.RxView
import com.mohammedalaa.seekbar.DoubleValueSeekBarView
import com.mohammedalaa.seekbar.OnDoubleValueSeekBarChangeListener
import kotlinx.android.synthetic.main.fragment_select_attributes.view.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class SelectAttributesFragment : Fragment() {

    private companion object {
        private const val TAG = "MySelectAttr"
    }

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_select_attributes, container, false)

        initListeners()

        if (Singleton.skillLevel == "beginner") {
            changeSelection(
                rootView.cont_beginner,
                rootView.tv_beginner,
                rootView.cont_intermediate,
                rootView.tv_intermediate,
                rootView.cont_expert,
                rootView.tv_expert
            )
        } else if (Singleton.skillLevel == "intermediate") {
            changeSelection(
                rootView.cont_intermediate,
                rootView.tv_intermediate,
                rootView.cont_beginner,
                rootView.tv_beginner,
                rootView.cont_expert,
                rootView.tv_expert
            )
        } else if (Singleton.skillLevel == "expert") {
            changeSelection(
                rootView.cont_expert,
                rootView.tv_expert,
                rootView.cont_beginner,
                rootView.tv_beginner,
                rootView.cont_intermediate,
                rootView.tv_intermediate
            )
        }
        if (Singleton.gender == "male") {
            changeSelection(
                rootView.cont_male,
                rootView.tv_male,
                rootView.cont_female,
                rootView.tv_female,
                rootView.cont_unisex,
                rootView.tv_unisex
            )
        } else if (Singleton.gender == "female") {
            changeSelection(
                rootView.cont_female,
                rootView.tv_female,
                rootView.cont_male,
                rootView.tv_male,
                rootView.cont_unisex,
                rootView.tv_unisex
            )
        } else if (Singleton.gender == "unisex") {
            changeSelection(
                rootView.cont_unisex,
                rootView.tv_unisex,
                rootView.cont_female,
                rootView.tv_female,
                rootView.cont_male,
                rootView.tv_male
            )
        }

        return rootView
    }

    fun getAgeRangeAndPlayersCount() {
        Singleton.ageMin = rootView.double_range_seekbar.currentMinValue.toString()
        Singleton.ageMax = rootView.double_range_seekbar.currentMaxValue.toString()
        Singleton.totalPlayers = rootView.et_total.text.toString()
        Singleton.confirmedPlayers = rootView.et_confirmed.text.toString()
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(rootView.cont_beginner).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            Singleton.skillLevel = "beginner"

            changeSelection(
                rootView.cont_beginner,
                rootView.tv_beginner,
                rootView.cont_intermediate,
                rootView.tv_intermediate,
                rootView.cont_expert,
                rootView.tv_expert
            )
        }
        RxView.clicks(rootView.cont_intermediate).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            Singleton.skillLevel = "intermediate"

            changeSelection(
                rootView.cont_intermediate,
                rootView.tv_intermediate,
                rootView.cont_beginner,
                rootView.tv_beginner,
                rootView.cont_expert,
                rootView.tv_expert
            )
        }
        RxView.clicks(rootView.cont_expert).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            Singleton.skillLevel = "expert"

            changeSelection(
                rootView.cont_expert,
                rootView.tv_expert,
                rootView.cont_beginner,
                rootView.tv_beginner,
                rootView.cont_intermediate,
                rootView.tv_intermediate
            )
        }

        RxView.clicks(rootView.cont_male).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            Singleton.gender = "male"

            changeSelection(
                rootView.cont_male,
                rootView.tv_male,
                rootView.cont_female,
                rootView.tv_female,
                rootView.cont_unisex,
                rootView.tv_unisex
            )
        }
        RxView.clicks(rootView.cont_female).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            Singleton.gender = "female"

            changeSelection(
                rootView.cont_female,
                rootView.tv_female,
                rootView.cont_male,
                rootView.tv_male,
                rootView.cont_unisex,
                rootView.tv_unisex
            )
        }
        RxView.clicks(rootView.cont_unisex).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            Singleton.gender = "unisex"

            changeSelection(
                rootView.cont_unisex,
                rootView.tv_unisex,
                rootView.cont_female,
                rootView.tv_female,
                rootView.cont_male,
                rootView.tv_male
            )
        }

        rootView.double_range_seekbar.setOnRangeSeekBarViewChangeListener(object :
            OnDoubleValueSeekBarChangeListener {
            override fun onStartTrackingTouch(
                seekBar: DoubleValueSeekBarView?,
                min: Int,
                max: Int
            ) {

            }

            override fun onStopTrackingTouch(seekBar: DoubleValueSeekBarView?, min: Int, max: Int) {

            }

            override fun onValueChanged(
                seekBar: DoubleValueSeekBarView?,
                min: Int,
                max: Int,
                fromUser: Boolean
            ) {
                Singleton.ageMin = min.toString()
                Singleton.ageMax = max.toString()
            }
        })

        rootView.rb_public.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Singleton.eventType = "public"
                Singleton.isInvitesActivityOpened = false
                rootView.rb_invites_only.isChecked = false
            }
        }

        rootView.rb_invites_only.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "isInvitesActivityOpened: ${Singleton.isInvitesActivityOpened}")
            if (isChecked) {
                Singleton.eventType = "invites"
                rootView.rb_public.isChecked = false

                if (!Singleton.isInvitesActivityOpened)
                    (requireActivity() as BaseActivity).startActivity(
                        requireContext(),
                        SelectCommunityGroupActivity::class.java,
                        false,
                        1
                    )

                Singleton.isInvitesActivityOpened = true
            }
        }
    }

    private fun changeSelection(
        viewToSelect: View,
        textViewToSelect: TextView,
        viewToDeselectOne: View,
        textViewToDeselectOne: TextView,
        viewToDeselectTwo: View?,
        textViewToDeselectTwo: TextView?
    ) {
        viewToSelect.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_grad_purple)
        textViewToSelect.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        viewToDeselectOne.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.stroke_purple_5_radius)
        textViewToDeselectOne.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.purple_dark_3
            )
        )
        viewToDeselectTwo?.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.stroke_purple_5_radius)
        textViewToDeselectTwo?.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.purple_dark_3
            )
        )
    }
}