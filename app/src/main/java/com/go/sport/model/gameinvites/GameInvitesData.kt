package com.go.sport.model.gameinvites


import com.google.gson.annotations.SerializedName

data class GameInvitesData(
    @SerializedName("accept_decline")
    val acceptDecline: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("game_id")
    val gameId: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user")
    val user: GameInvitesUser,
    @SerializedName("user_id")
    val userId: String
)