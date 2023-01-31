package com.go.sport.model.mygames


import com.go.sport.model.getfeatures.GetFeaturesData
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("booking_id")
    val bookingId: String? = "",
    @SerializedName("complete_time")
    val completeTime: String? = "",
    @SerializedName("created_at")
    val createdAt: String? = "",
    @SerializedName("currency")
    val currency: String? = "",
    @SerializedName("date")
    val date: String? = "",
    @SerializedName("detail")
    val detail: Detail? = null,
    @SerializedName("duration")
    val duration: String? = "",
    @SerializedName("facility")
    val facility: Facility? = null,
    @SerializedName("facility_id")
    val facilityId: String? = "",
    @SerializedName("facility_title")
    val facilityTitle: String? = "",
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("image")
    val image: String? = "",
    @SerializedName("join_count")
    val joinCount: String? = "",
    @SerializedName("slot")
    val slot: List<Slot>? = emptyList(),
    @SerializedName("sport")
    val sport: Sport? = null,
    @SerializedName("sports_id")
    val sportsId: String? = "",
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("type")
    val type: String? = "",
    @SerializedName("updated_at")
    val updatedAt: String? = "",
    @SerializedName("user")
    val user: User? = null,
    @SerializedName("user_id")
    val userId: String? = "",
    @SerializedName("unformatted_date")
    val unformatted_date: String? = "",
    @SerializedName("features")
    val features: ArrayList<GetFeaturesData>,
    @SerializedName("lat_lng")
    val lat_lng: String
)