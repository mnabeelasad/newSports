package com.go.sport.model.play

data class PlayUser(
    val created_at: String,
    val deleted_at: Any,
    val detail: PlayDetailX,
    val email: String,
    val email_verified_at: Any,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val level: Int,
    val phone_number: String,
    val reward_points: String,
    val updated_at: String
)