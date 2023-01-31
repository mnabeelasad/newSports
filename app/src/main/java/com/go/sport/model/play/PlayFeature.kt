package com.go.sport.model.play

data class PlayFeature(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val pivot: PlayPivot,
    val status: String,
    val updated_at: String
)