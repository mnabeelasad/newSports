package com.go.sport.model.filter.community


import com.google.gson.annotations.SerializedName

data class CommunityFilterUser(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("deleted_at")
    val deletedAt: Any,
    @SerializedName("detail")
    val detail: CommunityFilterDetail,
    @SerializedName("email")
    val email: String,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: Any,
    @SerializedName("facebook_token")
    val facebookToken: String,
    @SerializedName("fcm_token")
    val fcmToken: Any,
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
    @SerializedName("sport")
    val sport: List<CommunityFilterSport>,
    @SerializedName("updated_at")
    val updatedAt: String
)