package com.go.sport.ui.dashboard.fragments.book.summary

//import com.go.sport.base.BaseActivity
//
//class BookNowSummaryActivity : BaseActivity() {
//
//    private val amenities = arrayListOf<MyBookingFeature>()
//
//    private var from = ""
//    private var selectedTimeSlotStartTime = ""
//    private var selectedTimeSlotEndTime = ""
//    private var selectedTimeSlotDuration = ""
//    private var selectedPitch = ""
//    private var selectedDate = ""
//    private var facilityId = ""
//    private var address = ""
//
//    private lateinit var myBooking: MyBookings
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_book_now_summary)
//
//        init()
//        initRecyclerView()
//        initListeners()
////        bookFacility()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//    }
//
//    private fun init() {
//        from = intent?.extras?.getString("from") ?: ""
//        selectedTimeSlotStartTime = intent?.extras?.getString("startTime") ?: ""
//        selectedTimeSlotEndTime = intent?.extras?.getString("endTime") ?: ""
//        selectedTimeSlotDuration = intent?.extras?.getString("duration") ?: ""
//        selectedPitch = intent?.extras?.getString("pitch") ?: ""
//        selectedDate = intent?.extras?.getString("date") ?: ""
//        facilityId = intent?.extras?.getString("id") ?: ""
//        address = intent?.extras?.getString("address") ?: ""
//
//        if (from == "hostActivity")
//            cont_create_activity.visibility = View.GONE
//
//        if (selectedTimeSlotStartTime.isNotEmpty()) {
//            Glide.with(this).load(MySharedPreference(this).getUserObject()?.detail?.profile_image)
//                .into(iv_image)
//            tv_user.text =
//                MySharedPreference(this).getUserObject()?.first_name + " " + MySharedPreference(this).getUserObject()?.first_name
//            tv_gender.text = MySharedPreference(this).getUserObject()?.detail?.gender?.capitalize()
//            tv_age.text = getAgeFromDOB(
//                MySharedPreference(this).getUserObject()?.detail?.date_of_birth?.split("-")?.get(0)
//                    ?.toInt() ?: 0,
//                MySharedPreference(this).getUserObject()?.detail?.date_of_birth?.split("-")?.get(1)
//                    ?.toInt() ?: 0,
//                MySharedPreference(this).getUserObject()?.detail?.date_of_birth?.split("-")?.get(2)
//                    ?.toInt() ?: 0
//            )
//            tv_title.text =
//                Singleton.selectedSportsName
//            tv_location.text = Singleton.selectedVenueAddress
//            tv_time.text = "$selectedTimeSlotStartTime-$selectedTimeSlotEndTime"
//            tv_duration.text = "$selectedTimeSlotDuration"
//            tv_pitch.text = selectedPitch
//            tv_time.text = selectedDate
//
//            Singleton.selectedFacilities.map {
//                amenities.add(
//                    MyBookingFeature(
//                        it.created_at,
//                        it.id.toInt(),
//                        it.image,
//                        it.name,
//                        null,
//                        it.status.toString(),
//                        it.updated_at
//                    )
//                )
//            }
//        } else {
//            Glide.with(this).load(myBooking.data.user?.detail?.profileImage).into(iv_image)
//            tv_user.text = myBooking.user?.firstName + " " + myBooking.user?.lastName
//            tv_gender.text = myBooking.user?.detail?.gender?.capitalize()
//            tv_age.text = getAgeFromDOB(
//                myBooking.user?.detail?.dateOfBirth?.split("-")?.get(0)?.toInt() ?: 0,
//                myBooking.user?.detail?.dateOfBirth?.split("-")?.get(1)?.toInt() ?: 0,
//                myBooking.user?.detail?.dateOfBirth?.split("-")?.get(2)?.toInt() ?: 0
//            )
//            tv_title.text =
//                if (!myBooking.title.isNullOrEmpty()) myBooking.title else Singleton.selectedSportsName
//            tv_location.text =
//                myBooking.facility?.address
//
//            tv_time.text = myBooking.completeTime
//            tv_time.text = myBooking.date
//            tv_duration.text = "${myBooking.duration}"
//            tv_pitch.text = myBooking.pitchSize
//
//            amenities.clear()
//            myBooking.facility?.feature?.let {
//                amenities.addAll(
//                    it
//                )
//            }
//        }
//
//        rv_amenities.adapter?.notifyDataSetChanged()
//    }
//
//    private fun initRecyclerView() {
//        rv_amenities.apply {
//            adapter = VenueFacilitiesAdapter(this@BookNowSummaryActivity, amenities)
//        }
//    }
//
//    @SuppressLint("CheckResult")
//    private fun initListeners() {
//        RxView.clicks(iv_back).throttleFirst(2, TimeUnit.SECONDS).subscribe {
//            super.onBackPressed()
//        }
//
//        RxView.clicks(cont_home).throttleFirst(2, TimeUnit.SECONDS).subscribe {
//            startActivityFinishAll(this, DashboardActivity::class.java, true, -1)
//        }
//
//        RxView.clicks(cont_create_activity).throttleFirst(2, TimeUnit.SECONDS).subscribe {
////            bookFacility()
//            Singleton.from = "book"
//            startActivity(this, HostActivityActivity::class.java, true, 1)
//        }
//    }
//
//    private fun clearSingleton() {
//        Singleton.selectedSportsName = ""
//        Singleton.selectedSportsId = ""
//        Singleton.selectedVenueId = ""
//        Singleton.selectedVenueName = ""
//        Singleton.selectedVenueAddress = ""
//        Singleton.selectedPitchId = "0"
//        Singleton.selectedPitch = ""
//        Singleton.selectedDate = ""
//        Singleton.gameCost = "0"
//        Singleton.costPerPlay = "0"
//        Singleton.selectedFacilities = arrayListOf()
//        Singleton.selectedFacilitiesId = arrayListOf()
//        Singleton.selectedTimeSlots = arrayListOf()
//        Singleton.selectedTimeSlotStartTime = ""
//        Singleton.selectedTimeSlotEndTime = ""
//        Singleton.selectedTimeSlotDuration = ""
//        Singleton.additionalInfo = "none"
//        Singleton.skillLevel = ""
//        Singleton.totalPlayers = ""
//        Singleton.confirmedPlayers = ""
//        Singleton.ageMin = ""
//        Singleton.ageMax = ""
//        Singleton.gender = ""
//        Singleton.eventType = ""
//        Singleton.pricingType = ""
//        Singleton.startTimingIn = ""
//        Singleton.startTimingOut = ""
//        Singleton.pitchCourt = ""
//    }
//
//
//}