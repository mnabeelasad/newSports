package com.go.sport.model.getfacilitydata


import com.google.gson.annotations.SerializedName

data class GetFacilityDataPitchsize(
    val created_at: String,
    val facility_id: String,
    val id: Int,
    val name: String,
    val status: Boolean,
    val updatedAt: String,
    var isSelected: Boolean = false
)