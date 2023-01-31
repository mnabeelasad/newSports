package com.go.sport.model.filter.game


import com.google.gson.annotations.SerializedName

data class GameFilterGame(
    @SerializedName("booking_id")
    val bookingId: String,
    @SerializedName("complete_time")
    val completeTime: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("detail")
    val detail: GameFilterDetail,
    @SerializedName("duration")
    val duration: String,
    @SerializedName("facility")
    val facility: GameFilterFacility,
    @SerializedName("facility_id")
    val facilityId: String,
    @SerializedName("facility_title")
    val facilityTitle: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("join_count")
    val joinCount: String,
    @SerializedName("slot")
    val slot: List<GameFilterSlot>,
    @SerializedName("sport")
    val sport: GameFilterSport,
    @SerializedName("sports_id")
    val sportsId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user")
    val user: GameFilterUser,
    @SerializedName("user_id")
    val userId: String
)