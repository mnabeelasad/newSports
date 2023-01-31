package com.go.sport.model.userprofile

data class GetUserProfileDetail(
    val country_id: String,
    val created_at: String,
    val date_of_birth: String,
    val gender: String,
    val id: Int,
    val nick_name: String,
    val profile_image: String,
    val updated_at: String,
    val user_id: String
)