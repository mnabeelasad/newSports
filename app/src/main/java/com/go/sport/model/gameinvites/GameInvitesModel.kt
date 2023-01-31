package com.go.sport.model.gameinvites


import com.google.gson.annotations.SerializedName

data class GameInvitesModel(
    @SerializedName("data")
    val `data`: List<GameInvitesData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)