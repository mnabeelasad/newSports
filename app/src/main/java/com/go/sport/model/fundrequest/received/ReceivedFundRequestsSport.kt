package com.go.sport.model.fundrequest.received


import com.google.gson.annotations.SerializedName

data class ReceivedFundRequestsSport(
    @SerializedName("court_type")
    val courtType: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String
)