package com.go.sport.model.filter.facility


import com.google.gson.annotations.SerializedName

data class FacilityFilterModel(
    @SerializedName("facilities")
    val facilities: List<FacilityFilterFacility>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)