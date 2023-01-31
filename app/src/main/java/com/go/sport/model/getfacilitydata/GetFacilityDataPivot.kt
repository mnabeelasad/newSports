package com.go.sport.model.getfacilitydata


import com.google.gson.annotations.SerializedName

data class GetFacilityDataPivot(
    @SerializedName("facility_id")
    val facilityId: String,
    @SerializedName("feature_id")
    val featureId: String
)