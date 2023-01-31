package com.go.sport.model.fundrequest.received


import com.google.gson.annotations.SerializedName

data class ReceivedFundRequestsDetail(
    @SerializedName("age")
    val age: Any,
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
    @SerializedName("location")
    val location: Any,
    @SerializedName("nick_name")
    val nickName: String,
    @SerializedName("profile_image")
    val profileImage: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: String
)