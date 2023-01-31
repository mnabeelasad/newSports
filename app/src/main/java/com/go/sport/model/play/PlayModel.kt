package com.go.sport.model.play

import com.google.gson.annotations.SerializedName

data class PlayModel(
    @SerializedName("data")
    val data: List<PlayData>? = emptyList(),
    @SerializedName("Games")
    val games: List<PlayData>? = emptyList(),
    @SerializedName("games")
    val mGames: List<PlayData>? = emptyList(),
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)