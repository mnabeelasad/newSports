package com.go.sport.model.getfeatures

data class GetFeaturesData(
    val created_at: String,
    val id: String,
    val image: String,
    val name: String,
    val status: Boolean,
    var isSelected: Boolean,
    val updated_at: String
)