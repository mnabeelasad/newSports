package com.go.sport.model.gamejoins


import com.google.gson.annotations.SerializedName

data class GameJoinsOrganiser(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("deleted_at")
    val deletedAt: Any,
    @SerializedName("detail")
    val detail: GameJoinsDetail,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: Any,
    @SerializedName("facebook_token")
    val facebookToken: String,
    @SerializedName("fcm_token")
    val fcmToken: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("google_token")
    val googleToken: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("level")
    val level: Int,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("reward_points")
    val rewardPoints: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_agent")
    val userAgent: String
)