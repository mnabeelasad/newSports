package com.go.sport.model.userprofile

data class GetUserProfileData(
    val created_at: String,
    val deleted_at: Any,
    val detail: GetUserProfileDetail,
    val email: String,
    val email_verified_at: Any,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val level: Int,
    val phone_number: String,
    val reward_points: String,
    val sport: List<GetUserProfileSport>,
    val updated_at: String
)