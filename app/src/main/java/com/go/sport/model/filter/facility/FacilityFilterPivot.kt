package com.go.sport.model.filter.facility


import com.google.gson.annotations.SerializedName

data class FacilityFilterPivot(
    @SerializedName("facility_id")
    val facilityId: String,
    @SerializedName("feature_id")
    val featureId: String
)