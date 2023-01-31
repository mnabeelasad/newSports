package com.go.sport.model.signup

data class SignUpModel(
    val message: String,
    val status: Boolean,
    val user: SignUpUser?
)