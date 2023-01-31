package com.go.sport.model.getfacilities


import com.google.gson.annotations.SerializedName

data class GetFacilitiesFeatures(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("facility_id")
    val facilityId: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("sport")
    val sport: GetFacilitiesSport,
    @SerializedName("sport_id")
    val sportId: String,
    @SerializedName("updated_at")
    val updatedAt: String
)