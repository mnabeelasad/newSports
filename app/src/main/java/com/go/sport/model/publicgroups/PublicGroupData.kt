package com.go.sport.model.publicgroups

data class PublicGroupData(
    val action: String,
    val created_at: String,
    val created_by: String,
    val id: Int,
    val members: Int,
    val name: String,
    val owner_name: String,
    val type: String,
    val updated_at: String
)