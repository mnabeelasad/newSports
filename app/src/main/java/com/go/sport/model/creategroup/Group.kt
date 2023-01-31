package com.go.sport.model.creategroup

data class Group(
    val action: String,
    val created_at: String,
    val created_by: Int,
    val id: Int,
    val name: String,
    val owner_name: String,
    val type: String,
    val updated_at: String
)