package com.go.sport.model.userprofile

data class GetUserProfileModel(
    val `data`: GetUserProfileData,
    val message: String,
    val status: Boolean
)