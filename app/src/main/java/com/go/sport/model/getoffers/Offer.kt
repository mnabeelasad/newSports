package com.go.sport.model.getoffers


import com.google.gson.annotations.SerializedName

data class Offer(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("facility_id")
    val facilityId: String,
    @SerializedName("heading")
    val heading: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("media_links")
    val mediaLinks: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("watsapp_number")
    val whatsapp: String
)