package com.go.sport.model.leaderboardnew

data class PlayerFirst(
    val created_at: String,
    val deleted_at: Any,
    val detail: LeaderboardDetail,
    val email: String,
    val email_verified_at: Any,
    val facebook_token: String,
    val fcm_token: String,
    val first_name: String,
    val google_token: String,
    val id: Int,
    val last_name: String,
    val level: Int,
    val phone_number: String,
    val position: Int,
    val reward_points: String,
    val updated_at: String,
    val user_agent: String
)