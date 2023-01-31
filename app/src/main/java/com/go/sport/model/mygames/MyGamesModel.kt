package com.go.sport.model.mygames


import com.google.gson.annotations.SerializedName

data class MyGamesModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("my_games")
    val myGames: MyGames,
    @SerializedName("status")
    val status: Boolean
)