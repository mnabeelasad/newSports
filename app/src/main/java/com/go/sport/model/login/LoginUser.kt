package com.go.sport.model.login

data class LoginUser(
    val created_at: String,
    val deleted_at: Any,
    val detail: LoginDetail,
    val email: String,
    val email_verified_at: Any,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val token: String,
    val updated_at: String
)