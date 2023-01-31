package com.go.sport.model.gamejoins


import com.google.gson.annotations.SerializedName

data class GameJoinsData(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("game_id")
    val gameId: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("join_count")
    val joinCount: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user")
    val user: GameJoinsUser,
    @SerializedName("user_id")
    val userId: String
)