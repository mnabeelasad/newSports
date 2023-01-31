package com.go.sport.model.getusers

data class GetUsersModel(
    val `data`: List<GetUsersData>? = emptyList(),
    val user: List<GetUsersData>? = emptyList(),
    val message: String,
    val status: Boolean
)