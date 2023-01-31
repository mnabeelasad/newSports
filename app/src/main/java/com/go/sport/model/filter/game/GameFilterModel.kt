package com.go.sport.model.filter.game


import com.google.gson.annotations.SerializedName

data class GameFilterModel(
    @SerializedName("Games")
    val games: List<GameFilterGame>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)