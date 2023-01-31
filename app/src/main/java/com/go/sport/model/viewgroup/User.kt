package com.go.sport.model.viewgroup

import com.go.sport.model.getusers.GetUserSport

data class User(
    val created_at: String,
    val deleted_at: Any,
    val detail: Detail,
    val email: String,
    val email_verified_at: Any,
    val facebook_token: String,
    val fcm_token: Any,
    val first_name: String,
    val google_token: String,
    val id: Int,
    val last_name: String,
    val level: Int,
    val phone_number: String,
    val reward_points: String,
    val sport: List<GetUserSport>,
    val updated_at: String
)