package com.go.sport.ui.dashboard.fragments.home

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.HomeCategoriesModel
import com.go.sport.model.firebase.ChatHeadsModel
import com.go.sport.model.mygames.Data
import com.go.sport.network.APIManager
import com.go.sport.newui.NewBookingSummaryScreen
import com.go.sport.newui.NewSummaryScreen
import com.go.sport.newui.ViewBookingSummaryActivity
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.DashboardActivity
import com.go.sport.ui.dashboard.chats.ChatsActivity
import com.go.sport.ui.dashboard.editprofile.EditProfileActivity
import com.go.sport.ui.dashboard.fragments.home.adapter.CategoriesAdapter
import com.go.sport.ui.dashboard.fragments.home.adapter.MyGamesAdapter
import com.go.sport.ui.dashboard.fragments.home.adapter.MybookingAdapter
import com.go.sport.ui.dashboard.fragments.home.community.CommunityActivity
import com.go.sport.ui.dashboard.fragments.home.hostactivity.HostActivityActivity
import com.go.sport.ui.dashboard.fragments.home.leaderboard.LeaderBoardActivity
import com.go.sport.ui.dashboard.fragments.home.mygames.MyGamesActivity
import com.go.sport.ui.dashboard.fragments.home.mygroups.MyGroupsActivity
import com.go.sport.ui.dashboard.fragments.home.offers.OffersActivity
import com.go.sport.ui.dashboard.fragments.home.requestfunds.RequestFundsActivity
import com.go.sport.ui.dashboard.train.TrainActivity
import com.jakewharton.rxbinding2.view.RxView
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.menu_left_drawer.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(),
    CategoriesAdapter.CategoriesClickListener,
//    MyGamesAdapter.MyGamesClickListener,
    MybookingAdapter.MyBookingClickListener {

    private lateinit var rootView: View
    private val categories = ArrayList<HomeCategoriesModel>()
    private val myGames = arrayListOf<Data>()
    var chatHeadsList: ArrayList<ChatHeadsModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        addCategories()
        initViews()
        initListeners()



        return rootView
    }

    override fun onResume() {
        super.onResume()
        getMyProfile()
        myGames()
    }


    private fun addCategories() {
        categories.clear()
        categories.add(HomeCategoriesModel("Host Activity", R.drawable.icon_host_activity_top))
        categories.add(HomeCategoriesModel("Groups", R.drawable.icon_groups_top))
        categories.add(HomeCategoriesModel("Offers", R.drawable.icon_offer_top))
        categories.add(HomeCategoriesModel("Community", R.drawable.icon_community_top))
        categories.add(HomeCategoriesModel("Leaderboard", R.drawable.ic_leaderboard_new))
        categories.add(HomeCategoriesModel("Request funds", R.drawable.ic_request_funds))
    }

    private fun initViews() {
        rootView.rv_categories.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = CategoriesAdapter(requireContext(), this@HomeFragment, categories)
        }


        if (chatHeadsList.size == 0) {
            for (date in chatHeadsList) {
                val Fdate = (requireContext() as BaseActivity).getTime(date.date)
                val Tdate = (requireContext() as BaseActivity).getCurrentTime("hh:mm a")
                if (Fdate != Tdate) {
                    rootView.chat_dot.visibility = View.GONE
                } else {
                    rootView.chat_dot.visibility = View.VISIBLE
                }
            }
        }else{
            rootView.chat_dot.visibility = View.GONE
        }

        rootView.rv_my_games.apply {

            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = MyGamesAdapter(requireContext(), myGames) { myGame ->
                Singleton.homeClickedMyGamesModel = myGame
                if ((requireActivity() as DashboardActivity).slide.isMenuClosed)
                    if (myGame.type == "booking") {
                        (requireActivity() as DashboardActivity).startActivity(
                            requireContext(),
                            ViewBookingSummaryActivity::class.java,
                            false,
                            1
                        )
                    } else { Singleton.SummaryFrom = true
                            (requireActivity() as DashboardActivity).startActivity(
                        this.context,
                        NewSummaryScreen::class.java,
                        false,
                        1
                    )
                    }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(rootView.iv_hamburger).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            if ((activity as DashboardActivity).slide.isMenuClosed)
                (activity as DashboardActivity).slide.openMenu(true)
        }

        RxView.clicks(rootView.iv_chat).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (activity as DashboardActivity).startActivity(
                requireContext(),
                ChatsActivity::class.java,
                false,
                1
            )
        }

        RxView.clicks(rootView.cont_image).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (activity as DashboardActivity).startActivity(
                requireContext(),
                EditProfileActivity::class.java,
                false,
                1
            )
        }

        RxView.clicks(rootView.tv_view_all).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (activity as DashboardActivity).startActivity(
                requireContext(),
                MyGamesActivity::class.java,
                false,
                1
            )
        }

        RxView.clicks(rootView.cont_play).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (activity as DashboardActivity).fragNavController.switchTab(1)
            (activity as DashboardActivity).bottom_bar.setActiveItem(1)
        }

        RxView.clicks(rootView.cont_train).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (activity as DashboardActivity).startActivity(
                requireContext(),
                TrainActivity::class.java,
                false,
                1
            )
        }

        RxView.clicks(rootView.cont_book).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (activity as DashboardActivity).fragNavController.switchTab(2)
            (activity as DashboardActivity).bottom_bar.setActiveItem(2)
        }
    }

    private fun myGames() {
        (requireActivity() as DashboardActivity).pBar(1)
        APIManager.getMyGames(
            (requireActivity() as DashboardActivity).returnUserAuthToken(),
            1
        ) { result, error ->
            (requireActivity() as DashboardActivity).pBar(0)
            myGames.clear()

            if (error != null) {
                (requireActivity() as DashboardActivity).mOnError(error)
                return@getMyGames
            }

            if (result?.status == true) {

                myGames.addAll(result.myGames.data)

                if (myGames.size == 0) {
                    rootView.emptyGames.visibility = View.VISIBLE
                }
                rootView.rv_my_games.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun getMyProfile() {
        (requireActivity() as DashboardActivity).pBar(1)
        APIManager.myProfile((requireActivity() as DashboardActivity).returnUserAuthToken()) { result, error ->
            (requireActivity() as DashboardActivity).pBar(0)

            if (error != null) {
                (requireActivity() as DashboardActivity).mOnError(error)
                return@myProfile
            }

            if (result?.status == true) {
                val circularProgressDrawable = CircularProgressDrawable(requireContext())
                circularProgressDrawable.centerRadius = 25f
                circularProgressDrawable.start()

                if (result.data.detail != null) {
                    if (result.data.detail.profile_image != "") {
                        Glide.with(requireContext()).load(result.data.detail.profile_image)
                            .placeholder(circularProgressDrawable)
                            .into(rootView.iv_image)
                    } else {
                        Glide.with(requireContext()).load(R.drawable.avatar)
                            .placeholder(circularProgressDrawable)
                            .into(rootView.iv_image)
                    }
                    rootView.tv_user.text = "Welcome ${result.data.first_name}!"
                    rootView.tv_gender.text = result.data.detail.gender.capitalize()
                    rootView.tv_age.text = (requireActivity() as DashboardActivity).getAgeFromDOB(
                        result.data.detail.date_of_birth.split("-")[0].toInt(),
                        result.data.detail.date_of_birth.split("-")[1].toInt(),
                        result.data.detail.date_of_birth.split("-")[2].toInt()
                    ) + " Years"

                    Glide.with(requireContext()).load(result.data.detail.profile_image)
                        .placeholder(circularProgressDrawable)
                        .into(iv_image)
                    tv_user.text =
                        "${result.data.first_name.capitalize()} ${result.data.last_name.capitalize()}"
                    tv_gender.text = result.data.detail.gender.capitalize()
                    tv_age.text = (requireActivity() as DashboardActivity).getAgeFromDOB(
                        result.data.detail.date_of_birth.split("-")[0].toInt(),
                        result.data.detail.date_of_birth.split("-")[1].toInt(),
                        result.data.detail.date_of_birth.split("-")[2].toInt()
                    ) + " Years"
                }
            }
        }
    }

    override fun onCategoryItemClick(position: Int) {
        if ((requireActivity() as DashboardActivity).slide.isMenuClosed)
            when (position) {
                0 -> (requireActivity() as BaseActivity).startActivity(
                    requireContext(),
                    HostActivityActivity::class.java,
                    false,
                    1
                )
                1 -> (requireActivity() as BaseActivity).startActivity(
                    requireContext(),
                    MyGroupsActivity::class.java,
                    false,
                    1
                )
                2 -> (requireActivity() as BaseActivity).startActivity(
                    requireContext(),
                    OffersActivity::class.java,
                    false,
                    1
                )
                3 -> (requireActivity() as BaseActivity).startActivity(
                    requireContext(),
                    CommunityActivity::class.java,
                    false,
                    1
                )
                4 -> (requireActivity() as BaseActivity).startActivity(
                    requireContext(),
                    LeaderBoardActivity::class.java,
                    false,
                    1
                )
                5 -> (requireActivity() as BaseActivity).startActivity(
                    requireContext(),
                    RequestFundsActivity::class.java,
                    false,
                    1
                )
            }
    }

    override fun onMyBookingItemClick(position: Int) {

        if ((requireActivity() as DashboardActivity).slide.isMenuClosed)
            (requireActivity() as DashboardActivity).startActivity(
                requireContext(),
                ViewBookingSummaryActivity::class.java,
                false,
                1
            )
    }
}