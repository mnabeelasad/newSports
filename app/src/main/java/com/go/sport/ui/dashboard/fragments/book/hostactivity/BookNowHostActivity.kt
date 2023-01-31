package com.go.sport.ui.dashboard.fragments.book.hostactivity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getfacilitydata.GetFacilityDataPitchsize
import com.go.sport.model.getslots.GetSlotsSlot
import com.go.sport.network.APIManager
import com.go.sport.newui.NewBookingSummaryScreen
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.fragments.book.hostactivity.adapter.AvailablePitchesAdapter
import com.go.sport.ui.dashboard.fragments.book.hostactivity.adapter.TimeSlotsAdapter
import com.go.sport.ui.dashboard.fragments.home.hostactivity.HostActivityActivity
import com.jakewharton.rxbinding2.view.RxView
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import kotlinx.android.synthetic.main.activity_book_now_host.*
import kotlinx.android.synthetic.main.activity_book_now_host.tv_cost
import kotlinx.android.synthetic.main.activity_new_booking_summary_screen.*
import kotlinx.android.synthetic.main.activity_new_summary_screen.*
import kotlinx.android.synthetic.main.calender_item.view.*
import kotlinx.android.synthetic.main.fragment_select_date.view.*
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class BookNowHostActivity : BaseActivity(),
    AvailablePitchesAdapter.OnAvailablePitchesClickListener,
    TimeSlotsAdapter.OnTimeSlotsClickListener {

    private val pitches = ArrayList<GetFacilityDataPitchsize>()
    private val slots = arrayListOf<GetSlotsSlot>()
    private val selectedTimeSlotsIds = arrayListOf<String>()
    private val selectedTimeSlots = arrayListOf<GetSlotsSlot>()

    private var id = ""
    private var pitchId = ""
    private var selectedPitch = ""
    private var selectedDate = ""
    private var address = ""
    private var isPitchSelected = false
    private var isDateSelected = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_now_host)

        id = intent?.extras?.getString("id") ?: ""
        Singleton.bookingID = id
        address = intent?.extras?.getString("address") ?: ""
        Singleton.selectedBookingVenueAddress = address
        //  Singleton.featuresList
        initRecyclerView()
        initCalender()
        initListeners()
        mGetFacilityDetails()
    }

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var currentDate = 0
    lateinit var myCalendarViewManager: CalendarViewManager

    private fun initCalender() {
        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]
        currentDate = calendar[Calendar.DATE]

/*        val startDate = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)

        val endDate = Calendar.getInstance()
        endDate.add(Calendar.MONTH, 1)*/

        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                // return item layout files, which you have created
                return if (isSelected)
                    R.layout.calender_item
                else
                    R.layout.unselected_calender_item
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                holder.itemView.tv_date_calendar_item.text = DateUtils.getDayNumber(date)
                holder.itemView.tv_day_calendar_item.text = DateUtils.getDay3LettersName(date)

            }
        }

        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                val cal = Calendar.getInstance()
                cal.time = date
                return true
            }
        }
        val myCalendarChangesObserver = object : CalendarChangesObserver {
            override fun whenWeekMonthYearChanged(
                weekNumber: String,
                monthNumber: String,
                monthName: String,
                year: String,
                date: Date
            ) {
                tvmonth.text = monthName + " " + year
                super.whenWeekMonthYearChanged(weekNumber, monthNumber, monthName, year, date)

            }

            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                isDateSelected = true
                selectedDate =
                    "${DateUtils.getYear(date)}-${DateUtils.getMonthNumber(date)}-${
                        DateUtils.getDayNumber(
                            date
                        )
                    }"

                Singleton.selectedDate = selectedDate

                tvDate.text =
                    "${DateUtils.getDayNumber(date)} ${DateUtils.getMonthName(date)}, ${
                        DateUtils.getYear(
                            date
                        )
                    }"
                tvDay.text = DateUtils.getDayName(date)
                if (Singleton.from == "venue")
                    cont_time_venue.visibility = View.VISIBLE

                if (!onlyOnce) {
                    onlyOnce = true
                    if (isPitchSelected) {
                        mGetSlots()
                    } else {
                        warningToast("Please select pitch size to continue")
                    }
                }

                super.whenSelectionChanged(isSelected, position, date)
            }
        }

        val singleRowCalendar = main_single_row_calendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            init()
        }

        btnRight.setOnClickListener {
            singleRowCalendar.setDates(getDatesOfNextMonth())
            singleRowCalendar.init()
        }

        btnLeft.setOnClickListener {
            val c = Calendar.getInstance().get(Calendar.MONTH)
            if (c >= currentMonth) {
                warningToast("You can Not Move Back")
            } else {
                singleRowCalendar.setDates(getDatesOfPreviousMonth())
                singleRowCalendar.init()
            }
        }
    }

    var onlyOnce = false

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        return getCurrentDates(mutableListOf())
    }

    private fun getCurrentDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DATE, currentDate)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)
        if (Calendar.getInstance().get(Calendar.MONTH) == currentMonth)
            calendar.set(Calendar.DAY_OF_MONTH, currentDate)
        else
            calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

    private fun initRecyclerView() {
        rv_available_pitches.adapter =
            AvailablePitchesAdapter(
                this,
                pitches,
                this
            )
        rv_time_venue.adapter =
            TimeSlotsAdapter(
                this,
                slots,
                this
            )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }

    @SuppressLint("CheckResult")
    private fun initListeners() {
        RxView.clicks(iv_back).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            super.onBackPressed()
        }

        RxView.clicks(cont_next).throttleFirst(2, TimeUnit.SECONDS).subscribe {

            if (!isPitchSelected) {
                warningToast("Please select pitch size to continue")
                return@subscribe
            }
            if (!isDateSelected) {
                warningToast("Please select date to continue")
                return@subscribe
            }
            if (selectedTimeSlots.isEmpty()) {
                warningToast("Please select at least one time slot to continue")
                return@subscribe
            }
            closeKeyboard()
            startActivity(this, NewBookingSummaryScreen::class.java, false, 1)
        }
    }

    private fun mGetFacilityDetails() {
        pBar(1)
        APIManager.getFacilityData(
            returnUserAuthToken(),
            Singleton.bookingID,
        ) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@getFacilityData
            }

            if (result != null) {
                pitches.clear()

                pitches.addAll(result.facility.pitchsize)
                rv_available_pitches.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun mGetSlots() {
        pBar(1)
        APIManager.getSlots(
            returnUserAuthToken(),
            pitchId,
            Singleton.bookingID,
            selectedDate
        ) { result, error ->
            pBar(0)
            slots.clear()
            rv_time_venue.adapter?.notifyDataSetChanged()


            onlyOnce = false

            if (error != null) {
                mOnError(error)
                return@getSlots
            }

            if (result?.status == "true") {
                slots.clear()
                slots.addAll(result.slots)
                rv_time_venue.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun mGetSelectedSlotsPrice() {
        pBar(1)
        APIManager.getSelectedSlotsPrice(
            returnUserAuthToken(),
            selectedTimeSlotsIds.joinToString { it }
        ) { result, error ->
            pBar(0)

            if (error != null) {
                mOnError(error)
                return@getSelectedSlotsPrice
            }

            if (result?.status == true) {
                Singleton.gameCost = result.total.toString()
                tv_cost.text = "PKR ${result.total}"
            }
        }
    }


    override fun onAvailablePitchesItemClick(position: Int, pitch: GetFacilityDataPitchsize) {
        isPitchSelected = true
        pitchId = pitch.id.toString()

        Singleton.selectedPitchId = pitchId
        selectedPitch = pitch.name
        Singleton.selectedPitch = selectedPitch

        for (i in pitches)
            i.isSelected = false
        pitch.isSelected = true
        rv_available_pitches.adapter?.notifyDataSetChanged()

        if (isDateSelected) {
            mGetSlots()
        } else {
            warningToast("Please select date to continue")
        }
    }

    override fun onTimeSlotsItemClick(position: Int, slot: GetSlotsSlot) {
        if (slot.status != 1) {
            if (!slot.isSelected) {

                for (i in 0 until slots.size) {
                    slots[i].isSelected = false
                }

                selectedTimeSlotsIds.clear()
                selectedTimeSlots.clear()

                slot.isSelected = true

                selectedTimeSlotsIds.add(slot.id)
                selectedTimeSlots.add(slot)

                Singleton.selectedTimeSlotStartTime = slot.start
                Singleton.bookingSummaryPayment = slot.payment_type
                Singleton.selectedTimeSlotEndTime = slot.end
                Singleton.selectedTimeSlotDuration = slot.duration
            } else {
                slot.isSelected = false
                selectedTimeSlotsIds.clear()
                selectedTimeSlots.clear()
            }


        }

        rv_time_venue.adapter?.notifyDataSetChanged()

        if (selectedTimeSlotsIds.size > 0)
            mGetSelectedSlotsPrice()
        else if (selectedTimeSlotsIds.size == 0) {
            selectedTimeSlotsIds.clear()
        } else
            warningToast("Please select at least one time slot to continue")

        Singleton.selectedTimeSlots.clear()
        Singleton.selectedTimeSlots.addAll(selectedTimeSlotsIds)

        if (selectedTimeSlots.size > 0) {
            Singleton.selectedTimeSlotStartTime = selectedTimeSlots[0].start
            Singleton.selectedTimeSlotEndTime = selectedTimeSlots[0].end
            Singleton.selectedTimeSlotDuration = selectedTimeSlots[0].duration
        } else {
            tv_cost.text = "PKR 0"
        }
    }


}