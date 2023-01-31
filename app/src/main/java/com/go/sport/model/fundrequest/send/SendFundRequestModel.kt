package com.go.sport.model.fundrequest.send


import com.google.gson.annotations.SerializedName

data class SendFundRequestModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)