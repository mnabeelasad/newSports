package com.go.sport.model.getmyprofile

data class GetMyProfileData(
        val created_at: String,
        val deleted_at: Any,
        val detail: GetMyProfileDetail,
        val email: String,
        val email_verified_at: Any,
        val first_name: String,
        val id: Int,
        val last_name: String,
        val phone_number: String,
        val updated_at: String,
        val sport: List<GetMyProfileSport>
)