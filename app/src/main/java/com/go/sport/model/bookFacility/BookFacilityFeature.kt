package com.go.sport.model.bookFacility

data class BookFacilityFeature(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val pivot: Pivot,
    val status: String,
    val updated_at: String
)