package com.go.sport.model.signup

data class SignUpUser(
    val created_at: String,
    val detail: SignUpUserDetail,
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val phone_number: String,
    val token: String,
    val updated_at: String
)