package com.go.sport.ui.dashboard.editprofile.selectsports

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getsports.GetSportsSport
import com.go.sport.network.APIManager
import com.go.sport.ui.dashboard.editprofile.selectsports.adapter.SelectSportsAdapter
import com.go.sport.ui.signup.SignUpSingleton
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_select_sports.*
import kotlinx.android.synthetic.main.activity_select_sports.iv_back
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.concurrent.TimeUnit

class SelectSportsActivity : BaseActivity(),
        SelectSportsAdapter.OnSelectSportsClickListener {

    private val sports = arrayListOf<GetSportsSport>()
    private var selectedSportIds = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_sports)

        init()
        initRecyclerView()
        initListeners()
        mGetSports()
    }

    private fun init() {
        selectedSportIds = intent?.extras?.getStringArrayList("selectedSports") ?: arrayListOf()
    }

    private fun initRecyclerView() {
        rv_sports.adapter = SelectSportsAdapter(this, sports, this)
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(cont_select).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            mSelectSports()
        }

        /*RxView.clicks(iv_football).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            if (isFootballSelected) {
                imageViewAnimatedChange(
                        iv_football,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_football_purple_unselected
                        )
                )
                isFootballSelected = false
            } else {
                imageViewAnimatedChange(
                        iv_football,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_football_purple_selected
                        )
                )
                isFootballSelected = true
            }
        }

        RxView.clicks(iv_cricket).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            if (isCricketSelected) {
                imageViewAnimatedChange(
                        iv_cricket,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_cricket_purple_unselected
                        )
                )
                isCricketSelected = false
            } else {
                imageViewAnimatedChange(
                        iv_cricket,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_cricket_purple_selected
                        )
                )
                isCricketSelected = true
            }
        }

        RxView.clicks(iv_badminton).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            if (isBadmintonSelected) {
                imageViewAnimatedChange(
                        iv_badminton,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_badminton_purple_unselected
                        )
                )
                isBadmintonSelected = false
            } else {
                imageViewAnimatedChange(
                        iv_badminton,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_badminton_purple_selected
                        )
                )
                isBadmintonSelected = true
            }
        }

        RxView.clicks(iv_tennis).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            if (isTennisSelected) {
                imageViewAnimatedChange(
                        iv_tennis,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_tennis_purple_unselected
                        )
                )
                isTennisSelected = false
            } else {
                imageViewAnimatedChange(
                        iv_tennis,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_tennis_purple_selected
                        )
                )
                isTennisSelected = true
            }
        }

        RxView.clicks(iv_table_tennis).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            if (isTableTennisSelected) {
                imageViewAnimatedChange(
                        iv_table_tennis,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_tennis_table_purple_unselected
                        )
                )
                isTableTennisSelected = false
            } else {
                imageViewAnimatedChange(
                        iv_table_tennis,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_tennis_table_purple_selected
                        )
                )
                isTableTennisSelected = true
            }
        }

        RxView.clicks(iv_volley_ball).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            if (isVolleyBallSelected) {
                imageViewAnimatedChange(
                        iv_volley_ball,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_volley_ball_purple_unselected
                        )
                )
                isVolleyBallSelected = false
            } else {
                imageViewAnimatedChange(
                        iv_volley_ball,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_volley_ball_purple_selected
                        )
                )
                isVolleyBallSelected = true
            }
        }

        RxView.clicks(iv_basket_ball).throttleFirst(0, TimeUnit.SECONDS).subscribe {
            if (isBasketBallSelected) {
                imageViewAnimatedChange(
                        iv_basket_ball,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_basket_ball_purple_unselected
                        )
                )
                isBasketBallSelected = false
            } else {
                imageViewAnimatedChange(
                        iv_basket_ball,
                        BitmapFactory.decodeResource(
                                resources,
                                R.drawable.icon_basket_ball_purple_selected
                        )
                )
                isBasketBallSelected = true
            }
        }*/
    }

    private fun mGetSports() {
        pBar(1)

        APIManager.getSports(returnUserAuthToken()) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@getSports
            }

            if (result?.status == true) {
                sports.clear()
                sports.addAll(result.sports)

                sports.forEach { sport ->
                    selectedSportIds.forEach { id ->
                        if (id.toInt() == sport.id)
                            sport.isSelected = true
                    }
                }

                rv_sports.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun mSelectSports() {
        if (selectedSportIds.size == 0) {
            warningToast("Please select at least one to continue")
            return
        }

        val selectedSports = selectedSportIds.joinToString { it }

        pBar(1)
        APIManager.selectSports(returnUserAuthToken(), selectedSports) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@selectSports
            }

            if (result?.status == true) {
                //successToast(result.message)

                Handler().postDelayed({
                    super.onBackPressed()
                }, 800)
            }
        }
    }

    override fun onSelectSportsItemClick(position: Int, selectSport: GetSportsSport) {
        selectSport.isSelected = !selectSport.isSelected

        if (selectSport.isSelected)
            selectedSportIds.add(selectSport.id.toString())
        else
            selectedSportIds.remove(selectSport.id.toString())

        rv_sports.adapter?.notifyItemChanged(position)
    }
}