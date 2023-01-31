package com.go.sport.singleton

import com.go.sport.model.academies.AcademiesAcadmy
import com.go.sport.model.getfacilities.GetFacilitiesFeatures

import com.go.sport.model.getfeatures.GetFeaturesData
import com.go.sport.model.invites.InvitesInvite
import com.go.sport.model.mygames.Data
import com.go.sport.model.play.PlayData

object Singleton {
    var isBookNowFromBookActivityClicked = false
    var isInvitesActivityOpened = false
    var from = "location"
    var isFinish : Boolean = false
    var isPastGame : Boolean = false
    var sportsBook = ArrayList<GetFacilitiesFeatures>()
    var VenueLocation = ""
    var latLngFromLocation = ""
    var userGender = ""
    var latitude = ""
    var longitude = ""
    var sportsTitle = ""
    var gameId = ""
    var SummaryFrom : Boolean = false

    var bookingSummaryPayment = ""
    var sportsImage = ""
    var selectedSportsName = ""
    var selectedSportsId = ""
    var selectedVenueId = ""
    var selectedVenueName = ""
    var selectedVenueAddress = ""
    //zain
    var featuresList = ArrayList<GetFeaturesData>()

    var selectedBookingVenueAddress = ""
    var selectedPitchId = "0"
    var selectedPitch = ""
    var selectedDate = ""
    var gameCost = "0"
    var costPerPlay = ""
    var slotPrice = ""
    var features = arrayListOf<GetFeaturesData>()
    var facilityFeatures = ""
    var userName = ""

    var selectedFacilities = arrayListOf<GetFeaturesData>()
    var selectedFacilitiesId = arrayListOf<String>()
    var selectedTimeSlots = arrayListOf<String>()
    var userInvites = arrayListOf<String>()
    var groupInvites = arrayListOf<String>()
    var selectedTimeSlotStartTime = ""
    var selectedTimeSlotEndTime = ""
    var selectedTimeSlot = ""
    var selectedTimeSlotDuration = ""
    var additionalInfo = "none"
    var skillLevel = ""
    var totalPlayers = ""
    var confirmedPlayers = ""
    var ageMin = ""
    var ageMax = ""
    var gender = ""
    var eventType = ""
    var pricingType = ""
    var startTimingIn = ""
    var startTimingOut = ""
    var pitchCourt = ""
    var old_id = -1
    var bookingID = ""
    var timeStampStartTime : Long = 0
    var timeStampEndTime : Long = 0

    var homeClickedMyGamesModel: Data? = null
    var invitesInvite: InvitesInvite? = null
    var playData: PlayData? = null

    var academy: AcademiesAcadmy? = null
}