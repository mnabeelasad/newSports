package com.go.sport.model.filter.community


import com.google.gson.annotations.SerializedName

data class CommunityFilterModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("user")
    val user: List<CommunityFilterUser>
)