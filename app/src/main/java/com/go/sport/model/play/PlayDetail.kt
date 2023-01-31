package com.go.sport.model.play

data class PlayDetail(
    val additional_information: String,
    val address: String,
    val age_max: String,
    val age_min: String,
    val confirmed_players: String,
    val cost_per_play: String,
    val created_at: String,
    val event_type: String,
    val facility_features: String,
    val game_fee: String,
    val game_id: String,
    val gender: String,
    val id: Int,
    val payment_type: String,
    val pitch_id: String,
    val seleted_time_slots: String,
    val skills: String,
    val total_players: String,
    val updated_at: String,
    val start_timing_out :String,
    val end_timing_out :String,
    val pitch_court : String
)