package com.go.sport.model.mygames


import com.google.gson.annotations.SerializedName

data class Pivot(
    @SerializedName("facility_id")
    val facilityId: String,
    @SerializedName("feature_id")
    val featureId: String
)