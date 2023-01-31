package com.go.sport.model.getfacilitydata


import com.google.gson.annotations.SerializedName

data class GetFacilityDataSport(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("facility_id")
    val facilityId: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("sport_id")
    val sportId: String,
    @SerializedName("updated_at")
    val updatedAt: String
)