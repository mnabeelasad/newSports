package com.go.sport.model.gamejoins


import com.google.gson.annotations.SerializedName

data class GameJoinsDetail(
    @SerializedName("country_id")
    val countryId: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("date_of_birth")
    val dateOfBirth: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nick_name")
    val nickName: String,
    @SerializedName("profile_image")
    val profileImage: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: String
)