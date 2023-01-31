package com.go.sport.ui.dashboard.fragments.play

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getsports.GetSportsSport
import com.go.sport.model.play.PlayData
import com.go.sport.network.APIManager
import com.go.sport.newui.NewSummaryScreen
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.dashboard.fragments.home.hostactivity.HostActivityActivity
import com.go.sport.ui.dashboard.fragments.play.adapter.FilterAdapter
import com.go.sport.ui.dashboard.fragments.play.adapter.GamesAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.activity_my_games.*
import kotlinx.android.synthetic.main.activity_my_games.swipeRefreshLayout
import kotlinx.android.synthetic.main.bottom_sheet_filter.cont_apply
import kotlinx.android.synthetic.main.bottom_sheet_filter_play.*
import kotlinx.android.synthetic.main.bottom_sheet_filter_play.view.*
import kotlinx.android.synthetic.main.fragment_play.*
import kotlinx.android.synthetic.main.fragment_play.view.*
import kotlinx.android.synthetic.main.fragment_play.view.rv_games
import kotlinx.android.synthetic.main.fragment_play.view.tv_filter
import kotlinx.android.synthetic.main.row_invites.*
import kotlinx.android.synthetic.main.row_invites.view.*
import kotlinx.android.synthetic.main.row_my_booking.*
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 */
class PlayFragment : Fragment(),
    FilterAdapter.OnFiltersClickListener,
    GamesAdapter.GamesClickListener {

    private val filterIds = ArrayList<Int>()
    private val plays = arrayListOf<PlayData>()
    private val sports = arrayListOf<GetSportsSport>()

    private lateinit var rootView: View
    private lateinit var bsd: BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_play, container, false)

        refresh()
        initViews()
        mGetPlays()
        mGetSports()
        initFilterBottomSheet()
        initListeners()


        return rootView
    }

    fun refresh() {

        rootView.refresh_games.setOnRefreshListener {
            mGetSports(true)
            refresh_games.setRefreshing(false)
        }
    }

    override fun onResume() {
        super.onResume()
        mGetPlays()
    }

    private fun initViews() {
        rootView.rv_games.adapter = GamesAdapter(requireContext(), plays, this)
    }

    @SuppressLint("CheckResult")
    private fun initFilterBottomSheet() {
        bsd = BottomSheetDialog(requireContext())
        bsd.setContentView(R.layout.bottom_sheet_filter_play)
        (requireContext() as DashboardActivity).setFont(
            com.go.sport.constants.Constants.REGULAR,
            bsd.et_location
        )

        RxView.clicks(bsd.cont_apply).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (bsd.isShowing) {
                if (bsd.et_location.text.toString().isEmpty()) {
                    (requireContext() as DashboardActivity).warningToast("Invalid Location")
                    return@subscribe
                }
                mAdvancedGameFilter(
                    bsd.double_range_seekbar.currentMinValue.toString() + "," + bsd.double_range_seekbar.currentMaxValue.toString(),
                    bsd.et_location.text.toString()
                )
            }

            RxView.clicks(bsd.cont_clear_filter).throttleFirst(2, TimeUnit.SECONDS).subscribe {
                if (bsd.isShowing) {
                    if (bsd.et_location.text.toString().isNotEmpty()) {
                        bsd.et_location.text?.clear()
                        bsd.dismiss()
                        rootView.rv_games.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }

        bsd.setOnShowListener(DialogInterface.OnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout?
                ?: return@OnShowListener
            bottomSheet.background = null
        })
    }

    private fun mGetSports(isRefresh: Boolean = false) {
        (requireActivity() as DashboardActivity).pBar(1)
        APIManager.getSports((requireActivity() as DashboardActivity).returnUserAuthToken()) { result, error ->
            (requireContext() as BaseActivity).pBar(0)
            if (isRefresh)
                sports.clear()

            if (error != null) {
                (requireActivity() as DashboardActivity).mOnError(error)
                return@getSports
            }

            if (result?.status == true) {
                sports.addAll(result.sports)


                rootView.rv_filters.apply {
                    adapter = FilterAdapter(
                        requireContext(),
                        sports,
                        filterIds,
                        this@PlayFragment
                    )
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(rootView.tv_filter).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if (!bsd.isShowing)
                bsd.show()
        }

        RxView.clicks(rootView.cont_host_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (requireActivity() as BaseActivity).startActivity(
                requireContext(),
                HostActivityActivity::class.java,
                false,
                1
            )
        }
//        RxView.clicks(rootView.cont_filter).throttleFirst(2,TimeUnit.SECONDS).subscribe {
//            on
//        }
    }

    private fun mGameFilter(sportsId: String) {
        (requireActivity() as BaseActivity).pBar(1)
        APIManager.gameFilter(
            (requireActivity() as BaseActivity).returnUserAuthToken(),
            sportsId
        ) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)
            plays.clear()
            rootView.rv_games.adapter?.notifyDataSetChanged()

            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@gameFilter
            }

            if (result?.status == true) {
                result.games?.let { plays.addAll(it) }
                rootView.rv_games.adapter?.notifyDataSetChanged()

                if (bsd.et_location == null) {
                    plays.addAll(plays)
                    rootView.rv_games.adapter?.notifyDataSetChanged()
                }
                if (plays.isEmpty()) {
                    (requireActivity() as DashboardActivity).errorToast("No Play(s) found for applied filters!")
                }
            }
        }
    }

    private fun mAdvancedGameFilter(price: String, location: String) {
        bsd.dismiss()
        (requireActivity() as BaseActivity).pBar(1)
        APIManager.advancedGameFilter(
            (requireActivity() as BaseActivity).returnUserAuthToken(),
            price,
            location
        ) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)
            plays.clear()
            rootView.rv_games.adapter?.notifyDataSetChanged()

            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@advancedGameFilter
            }

            if (result?.status == true) {
                result.mGames?.let { plays.addAll(it) }
                rootView.rv_games.adapter?.notifyDataSetChanged()

                if (plays.isEmpty()) {
                    (requireActivity() as DashboardActivity).errorToast("No Play(s) found for applied filters!")
                }
            }
        }
    }

    private fun mGetPlays(isRefresh: Boolean = false) {
        (requireActivity() as BaseActivity).pBar(1)
        APIManager.getPlays((requireActivity() as BaseActivity).returnUserAuthToken()) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)
            if (isRefresh)
                plays.clear()
            rootView.rv_games.adapter?.notifyDataSetChanged()

            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@getPlays
            }

            if (result?.status == true) {
                plays.clear()
                result.data?.let { plays.addAll(it) }
                plays.reverse()

                rootView.rv_games.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun joinAGame(gameId: String) {
        (requireActivity() as BaseActivity).pBar(1)
        APIManager.joinGame(
            (requireActivity() as BaseActivity).returnUserAuthToken(),
            gameId
        ) { result, error ->
            (requireActivity() as BaseActivity).pBar(0)

            if (error != null) {
                (requireActivity() as BaseActivity).mOnError(error)
                return@joinGame
            }

            if (result?.status == true) {
                if (result.message == "Joined.") {
                    (requireActivity() as BaseActivity).successToast("Game Joined Successfully")
                }
                join_game.visibility = View.GONE
                leave_game.visibility = View.VISIBLE
                (requireContext() as BaseActivity).pBar(0)
                mGetPlays(true)
            }
        }
    }

    override fun onFilterItemClick(position: Int, getSportsSport: GetSportsSport) {

        sports.forEach { sport ->
            if (sport != getSportsSport)
                sport.isSelected = false
        }
        rootView.rv_filters.adapter?.notifyDataSetChanged()

        getSportsSport.isSelected = !getSportsSport.isSelected
        rootView.rv_filters.adapter?.notifyItemChanged(position)

        if (getSportsSport.isSelected) {
            val sportsIds = filterIds.joinToString { it.toString() }
            mGameFilter(getSportsSport.id.toString())
        } else {
            mGetPlays()
        }
    }

    override fun onGamesItemClick(play: PlayData) {
        Singleton.playData = play
        (requireContext() as DashboardActivity).startActivity(
            requireContext(),
            NewSummaryScreen::class.java,
            false,
            1
        )
    }

    override fun onGamesJoinGameClick(position: Int) {
        if (plays[position].detail.total_players != plays[position].detail.confirmed_players) {
            if (plays[position].detail.payment_type == "wallet") {
                (requireContext() as BaseActivity).disclaimer(
                    "Terms & Conditions\n\n" +
                            "In case of wallet payment, the users will be asked to pay the amount in order to confirm their participation. \n\n" +
                            "Payment collected from users will be credited to your GoPlay wallet upon completion of activity. These can be used for transactions that you make on GoPlay and cannot be redeemed by way of cash or bank transfers.\n\n" +
                            "In case there is a dispute with respect to payment or activity, GoPlay will investigate the matter and in doing so, reserves the right to take corrective measures to amicably resolve the dispute.\n\n"
                ) {
                    joinAGame(plays[position].detail.game_id)
                }
            } else if (plays[position].detail.payment_type == "both") {
                (requireContext() as BaseActivity).paymentToast(
                    "Please select one payment type to pay",
                    {
                        (requireContext() as BaseActivity).disclaimer(
                            "Terms & Conditions\n\n" +
                                    "In case of wallet payment, the users will be asked to pay the amount in order to confirm their participation. \n\n" +
                                    "Payment collected from users will be credited to your GoPlay wallet upon completion of activity. These can be used for transactions that you make on GoPlay and cannot be redeemed by way of cash or bank transfers.\n\n" +
                                    "In case there is a dispute with respect to payment or activity, GoPlay will investigate the matter and in doing so, reserves the right to take corrective measures to amicably resolve the dispute.\n\n"
                        ) {
                            joinAGame(plays[position].detail.game_id)
                        }
                    },
                    {
                        joinAGame(plays[position].detail.game_id)
                    })
            } else {
                joinAGame(plays[position].detail.game_id)
            }
        } else {
            (requireContext() as BaseActivity).infoToast("Player's join count is full")
        }
    }


    override fun onLeaveGame(position: Int) {

        if (plays[position].status == "confirmed") {
            leaveGame(plays[position].detail.game_id)
        } else {
            (requireActivity() as BaseActivity).warningToast("Activity has been canceled")
        }
    }

    private fun leaveGame(gameId: String) {
        (requireContext() as BaseActivity).pBar(1)
        APIManager.leaveGamePlay(
            (requireContext() as BaseActivity).returnUserAuthToken(), gameId
        ) { result, error ->
            (requireContext() as BaseActivity).pBar(0)

            if (error != null) {
                (requireContext() as BaseActivity).mOnError(error)
                return@leaveGamePlay
            }
            if (result?.status == true) {
                if (result.message == "Left game") {
                    (requireContext() as BaseActivity).successToast("Game Left Successfully")
                    join_game.visibility = View.VISIBLE
                    leave_game.visibility = View.GONE
                    (requireContext() as BaseActivity).pBar(0)
                    mGetPlays(true)
                }
            }
        }
    }
}