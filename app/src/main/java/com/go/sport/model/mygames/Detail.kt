package com.go.sport.model.mygames


import com.google.gson.annotations.SerializedName

data class Detail(
    @SerializedName("additional_information")
    val additionalInformation: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("age_max")
    val ageMax: String,
    @SerializedName("age_min")
    val ageMin: String,
    @SerializedName("confirmed_players")
    val confirmedPlayers: String,
    @SerializedName("cost_per_play")
    val costPerPlay: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("event_type")
    val eventType: String,
    @SerializedName("facility_features")
    val facilityFeatures: String,
    @SerializedName("game_fee")
    val gameFee: String,
    @SerializedName("game_id")
    val gameId: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("payment_type")
    val paymentType: String,
    @SerializedName("pitch_id")
    val pitchId: String,
    @SerializedName("seleted_time_slots")
    val seletedTimeSlots: String,
    @SerializedName("skills")
    val skills: String,
    @SerializedName("total_players")
    val totalPlayers: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("pitch_court")
    val pitchCourt: String? = "",
    @SerializedName("start_timing_out")
    val startTimingOut: String? = "",
    @SerializedName("end_timing_out")
    val endTimingOut: String? = ""
)