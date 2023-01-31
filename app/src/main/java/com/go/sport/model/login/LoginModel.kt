package com.go.sport.model.login

data class LoginModel(
    val user: LoginUser?,
    val message: String,
    val status: Boolean
)