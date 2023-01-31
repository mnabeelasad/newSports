package com.go.sport.model.filter.advanced


import com.google.gson.annotations.SerializedName

data class AdvancedGameFilterModel(
    @SerializedName("games")
    val games: List<AdvancedGameFilterGame>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)