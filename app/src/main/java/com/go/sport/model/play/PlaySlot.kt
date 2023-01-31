package com.go.sport.model.play

data class PlaySlot(
    val created_at: String,
    val date_event: String,
    val duration: String,
    val end: String,
    val facility_id: String,
    val id: Int,
    val name: String,
    val pitchsize_id: String,
    val pivot: PlayPivotX,
    val slot_price: String,
    val start: String,
    val status: String,
    val updated_at: String
)