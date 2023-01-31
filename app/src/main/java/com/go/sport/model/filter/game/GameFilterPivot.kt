package com.go.sport.model.filter.game


import com.google.gson.annotations.SerializedName

data class GameFilterPivot(
    @SerializedName("facility_id")
    val facilityId: String,
    @SerializedName("feature_id")
    val featureId: String
)