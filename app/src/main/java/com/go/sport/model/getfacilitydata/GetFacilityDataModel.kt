package com.go.sport.model.getfacilitydata


import com.google.gson.annotations.SerializedName

data class GetFacilityDataModel(
    @SerializedName("facility")
    val facility: GetFacilityDataFacility,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)