package com.go.sport.model.fundrequest.decline


import com.google.gson.annotations.SerializedName

data class DeclineFundRequestModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)