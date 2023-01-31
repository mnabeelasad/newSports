package com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.date

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.go.sport.R
import com.go.sport.base.BaseActivity
import com.go.sport.model.getfacilitydata.GetFacilityDataPitchsize
import com.go.sport.model.getslots.GetSlotsSlot
import com.go.sport.network.APIManager
import com.go.sport.singleton.Singleton
import com.go.sport.ui.dashboard.fragments.home.hostactivity.HostActivityActivity
import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.date.adapter.AvailablePitchesAdapter
import com.go.sport.ui.dashboard.fragments.home.hostactivity.fragments.date.adapter.TimeSlotsAdapter
import com.jakewharton.rxbinding2.view.RxView
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import com.skydoves.powermenu.PowerMenuItem
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.calender_item.view.*
import kotlinx.android.synthetic.main.fragment_select_date.*
import kotlinx.android.synthetic.main.fragment_select_date.view.*
import kotlinx.android.synthetic.main.row_venue_time.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class SelectDateFragment : Fragment(),
    AvailablePitchesAdapter.OnAvailablePitchesClickListener,
    TimeSlotsAdapter.OnTimeSlotsClickListener {

    private lateinit var rootView: View

    @RequiresApi(Build.VERSION_CODES.O)
    private val today = LocalDate.now()
    private val pitches = ArrayList<PowerMenuItem>()
    private var pitchesRecycler = ArrayList<GetFacilityDataPitchsize>()
    private val slots = arrayListOf<GetSlotsSlot>()
    private val selectedTimeSlotsIds = arrayListOf<String>()
    private val selectedTimeSlots = arrayListOf<GetSlotsSlot>()

    @RequiresApi(Build.VERSION_CODES.O)
    private val titleSameYearFormatter = DateTimeFormatter.ofPattern("MMMM")

    private var pitchId = ""
    private var selectedDate = ""
    private var isPitchSelected = false
    private var isDateSelected = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_select_date, container, false)

        initRecyclerViews()
        //  if (!this::myCalendarViewManager.isInitialized){
        initcalender()
        //   }
        listeners()

        if (Singleton.selectedTimeSlotStartTime != "") {
            rootView.tv_start_time.text = Singleton.selectedTimeSlotStartTime
            rootView.time_start.visibility = View.VISIBLE
        }

        if (Singleton.selectedTimeSlotEndTime != "") {
            rootView.tv_end_time.text = Singleton.selectedTimeSlotEndTime
            rootView.time_end.visibility = View.VISIBLE
        }
        if (Singleton.selectedDate != "") {
            isDateSelected = true
            rootView.tvDate.text = Singleton.selectedDate
        }
        return rootView
    }

    override fun onResume() {
        super.onResume()
        zain()
        if (Singleton.selectedVenueId.isNotEmpty())
            mGetFacilityDetails()
    }

    private fun zain() {
        if (Singleton.from == "location") {
            rootView.cont_pitch_location.visibility = View.VISIBLE
            rootView.content.visibility = View.VISIBLE
            rootView.cont_pitch_venue.visibility = View.GONE
            rootView.cont_time_location.visibility = View.VISIBLE
            rootView.cont_time_venue.visibility = View.GONE
            rootView.cont_cost_venue.visibility = View.GONE
        } else if (Singleton.from == "venue" || Singleton.from == "book") {
            rootView.cont_pitch_location.visibility = View.GONE
            rootView.content.visibility = View.GONE
            rootView.cont_pitch_venue.visibility = View.VISIBLE
            rootView.cont_time_location.visibility = View.GONE
            rootView.cont_time_venue.visibility = View.GONE
            rootView.cont_cost_venue.visibility = View.GONE
        }
    }

    fun getPrice() {
        if (Singleton.from == "location") {
            Singleton.gameCost = "0"
        }
    }

    fun getStartTime() {
        if (Singleton.from == "location") {
            val startTime = rootView.tv_start_time.text.toString()
            Singleton.selectedTimeSlotStartTime =
                if (startTime != "Select Start time") startTime else ""
        }
    }


    fun getEndTime() {
        if (Singleton.from == "location") {
            val endTime = rootView.tv_end_time.text.toString()
            Singleton.selectedTimeSlotEndTime =
                if (endTime != "Select End time") endTime else ""
        }
    }

    fun getPitch() {
        if (Singleton.from == "location") {
            Singleton.selectedPitch = rootView.tv_select_pitch.text.toString()
        }
    }

    fun getCost() {
        if (Singleton.from == "venue") {
            Singleton.gameCost = result_total.toString()
        }
    }

    @SuppressLint("CheckResult")
    private fun listeners() {
        RxView.clicks(rootView.tv_start_time).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (activity as HostActivityActivity).showTimePickerDialog(
                (activity as HostActivityActivity),
                rootView.tv_start_time
            ) {
//                (activity as BaseActivity).warningToast(it)
                Singleton.timeStampStartTime = it

            }
            rootView.time_start.visibility = View.VISIBLE
        }

        RxView.clicks(rootView.tv_end_time).throttleFirst(2, TimeUnit.SECONDS).subscribe {
            (activity as HostActivityActivity).showTimePickerDialog(
                (activity as HostActivityActivity),
                rootView.tv_end_time
            ) {
                Singleton.timeStampEndTime = it
            }
            rootView.time_end.visibility = View.VISIBLE
        }

//        RxView.clicks(rootView.tv_select_pitch).throttleFirst(2, TimeUnit.SECONDS).subscribe {
//            (activity as HostActivityActivity).popupDisplay(
//                (activity as HostActivityActivity),
//                rootView.tv_select_pitch,
//                rootView.tv_select_pitch,
//                false,
//                pitches,
//                BaseActivity.POPUPDISPLAY_MATCHCONT,
//                0
//            ) { selectedText ->
//                pitches.forEachIndexed { index, powerMenuItem ->
//                    if (powerMenuItem.title == selectedText) {
//                        Singleton.selectedPitchId = pitches[index].title
//                    }
//                }
//                rootView.tv_select_pitch.setText(selectedText)
//            }
//        }
    }

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private var currentDate = 0
    lateinit var myCalendarViewManager: CalendarViewManager
    private fun initcalender() {
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
                rootView.tvmonth.text = monthName + " " + year
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

                rootView.tvDate.text =
                    "${DateUtils.getDayNumber(date)} ${DateUtils.getMonthName(date)}, ${
                        DateUtils.getYear(date)
                    }"
                rootView.tvDay.text = DateUtils.getDayName(date)
                if (Singleton.from == "venue")
                    rootView.cont_time_venue.visibility = View.VISIBLE

                if (!onlyOnce) {
                    onlyOnce = true
                    if (Singleton.from == "venue") {
                        if (isPitchSelected) {
                            mGetSlots()
                        } else {
                            (requireActivity() as HostActivityActivity).warningToast("Please select pitch size to continue")
                        }
                    }
                }

                super.whenSelectionChanged(isSelected, position, date)
            }
        }

        val singleRowCalendar = rootView.main_single_row_calendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            init()
        }

        rootView.btnRight.setOnClickListener {
            singleRowCalendar.setDates(getDatesOfNextMonth())
            singleRowCalendar.init()
        }

        rootView.btnLeft.setOnClickListener {

            val c = Calendar.getInstance().get(Calendar.MONTH)
            if (c >= currentMonth) {
                (requireActivity() as BaseActivity).warningToast("You can Not Move Back")
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
        currentMonth--  // - because we want previous month
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

    private fun initPitches() {
        pitches.clear()

        pitches.add(PowerMenuItem("5x5"))
        pitches.add(PowerMenuItem("6x6"))
        pitches.add(PowerMenuItem("7x7"))
        pitches.add(PowerMenuItem("8x8"))
        pitches.add(PowerMenuItem("9x9"))
    }

    private fun initRecyclerViews() {
        initPitches()

        rootView.rv_available_pitches.apply {
            adapter =
                AvailablePitchesAdapter(requireContext(), pitchesRecycler, this@SelectDateFragment)
        }

        rootView.rv_time_venue.apply {
            adapter = TimeSlotsAdapter(requireContext(), slots, this@SelectDateFragment)
        }
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

    private fun mGetFacilityDetails() {
        (requireActivity() as HostActivityActivity).pBar(1)
        APIManager.getFacilityData(
            (requireActivity() as HostActivityActivity).returnUserAuthToken(),
            Singleton.selectedVenueId
        ) { result, error ->
            (requireActivity() as HostActivityActivity).pBar(0)

            if (error != null) {
                (requireActivity() as HostActivityActivity).mOnError(error)
                return@getFacilityData
            }

            if (result != null) {
                pitchesRecycler.clear()
                pitchesRecycler.addAll(result.facility.pitchsize)
                rootView.rv_available_pitches.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun mGetSlots() {
        (requireActivity() as HostActivityActivity).pBar(1)
        Handler().postDelayed({
            APIManager.getSlots(
                (requireActivity() as HostActivityActivity).returnUserAuthToken(),
                pitchId,
                Singleton.selectedVenueId,
                selectedDate
            ) { result, error ->
                (requireActivity() as HostActivityActivity).pBar(0)
                slots.clear()
                onlyOnce = false

                rootView.rv_time_venue.adapter?.notifyDataSetChanged()

                if (result != null) {
                    if (result.status == "true") {
                        slots.addAll(result.slots)

                        rootView.rv_time_venue.adapter?.notifyDataSetChanged()
                    } else {
                        slots.addAll(result.slots ?: ArrayList())

                        rootView.rv_time_venue.adapter?.notifyDataSetChanged()
                    }
                } else if (error != null) {
                    (requireActivity() as HostActivityActivity).mOnError(error)
                    return@getSlots
                }
            }
        }, 1200)

    }

    var result_total = 0
    private fun mGetSelectedSlotsPrice() {
        (requireActivity() as HostActivityActivity).pBar(1)
        APIManager.getSelectedSlotsPrice(
            (requireActivity() as HostActivityActivity).returnUserAuthToken(),
            selectedTimeSlotsIds.joinToString { it }
        ) { result, error ->
            (requireActivity() as HostActivityActivity).pBar(0)

            if (error != null) {
                (requireActivity() as HostActivityActivity).mOnError(error)
                return@getSelectedSlotsPrice
            }

            if (result?.status == true) {
                Singleton.gameCost = result.total.toString()
                rootView.tv_cost.text = "PKR ${result.total}"
                result_total = result.total
            }
        }
    }

    override fun onAvailablePitchesItemClick(position: Int, pitch: GetFacilityDataPitchsize) {
        isPitchSelected = true
        pitchId = pitch.id.toString()
        Singleton.selectedPitchId = pitchId
        Singleton.selectedPitch = pitch.name

        for (i in pitchesRecycler)
            i.isSelected = false
        pitch.isSelected = true
        rootView.rv_available_pitches.adapter?.notifyDataSetChanged()

        if (isDateSelected) {
            mGetSlots()
        } else {
            (requireActivity() as HostActivityActivity).warningToast("Please select date to continue")
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
                if (Singleton.from == "location") {
                    Singleton.selectedTimeSlotStartTime = slot.start
                    Singleton.selectedTimeSlotEndTime = slot.end
                    Singleton.selectedTimeSlotDuration = slot.duration

                } else if (Singleton.from == "venue") {
                    Singleton.selectedTimeSlotStartTime = slot.start
                    Singleton.selectedTimeSlotEndTime = slot.end
                    Singleton.selectedTimeSlotDuration = slot.duration
                    Singleton.gameCost = slot.slot_price
                    Singleton.bookingSummaryPayment = slot.payment_type

                }

            } else {


                slot.isSelected = false
                selectedTimeSlotsIds.clear()
                selectedTimeSlots.clear()

                Singleton.selectedTimeSlotStartTime = ""
                Singleton.selectedTimeSlotEndTime = ""
                Singleton.selectedTimeSlotDuration = ""
            }

        }

        rootView.rv_time_venue.adapter?.notifyDataSetChanged()

        if (selectedTimeSlotsIds.size > 0)
            mGetSelectedSlotsPrice()
        else if (selectedTimeSlotsIds.size == 0) {
            selectedTimeSlotsIds.clear()
        }

        Singleton.selectedTimeSlots.clear()
        Singleton.selectedTimeSlots.addAll(selectedTimeSlotsIds)

        if (selectedTimeSlots.size > 0) {
            Singleton.selectedTimeSlotStartTime = slot.start
            Singleton.selectedTimeSlotEndTime = slot.end
            Singleton.selectedTimeSlotDuration = slot.duration

        } else {
            rootView.tv_cost.text = "PKR 0"
        }
    }


}