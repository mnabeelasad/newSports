package com.go.sport.model.filter.advanced


import com.google.gson.annotations.SerializedName

data class AdvancedGameFilterSlot(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("date_event")
    val dateEvent: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("end")
    val end: String,
    @SerializedName("facility_id")
    val facilityId: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("pitchsize_id")
    val pitchsizeId: String,
    @SerializedName("pivot")
    val pivot: AdvancedGameFilterPivotX,
    @SerializedName("slot_price")
    val slotPrice: String,
    @SerializedName("start")
    val start: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String
)