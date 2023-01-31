package com.go.sport.model.gamejoins


import com.google.gson.annotations.SerializedName

data class GameJoinsModel(
    @SerializedName("data")
    val `data`: List<GameJoinsData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)