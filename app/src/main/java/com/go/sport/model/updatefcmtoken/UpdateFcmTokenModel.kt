package com.go.sport.model.updatefcmtoken


import com.google.gson.annotations.SerializedName

data class UpdateFcmTokenModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)