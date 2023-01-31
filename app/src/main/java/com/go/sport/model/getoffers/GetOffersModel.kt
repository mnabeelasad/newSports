package com.go.sport.model.getoffers


import com.google.gson.annotations.SerializedName

data class GetOffersModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("offer")
    val offer: ArrayList<Offer>,
    @SerializedName("status")
    val status: String
)