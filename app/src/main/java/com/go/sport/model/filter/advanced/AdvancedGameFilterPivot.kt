package com.go.sport.model.filter.advanced


import com.google.gson.annotations.SerializedName

data class AdvancedGameFilterPivot(
    @SerializedName("facility_id")
    val facilityId: String,
    @SerializedName("feature_id")
    val featureId: String
)