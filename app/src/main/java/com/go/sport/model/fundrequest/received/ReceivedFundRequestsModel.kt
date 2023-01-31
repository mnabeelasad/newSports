package com.go.sport.model.fundrequest.received


import com.google.gson.annotations.SerializedName

data class ReceivedFundRequestsModel(
    @SerializedName("data")
    val `data`: List<ReceivedFundRequestsData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)