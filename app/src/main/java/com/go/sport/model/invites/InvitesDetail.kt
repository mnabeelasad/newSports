package com.go.sport.model.invites

data class InvitesDetail(
    val additional_information: String,
    val address: String,
    val age_max: String,
    val age_min: String,
    val confirmed_players: String,
    val cost_per_play: String,
    val created_at: String,
    val end_timing_out: String,
    val event_type: String,
    val facility_features: String,
    val game_fee: String,
    val game_id: String,
    val gender: String,
    val id: Int,
    val payment_type: String,
    val pitch_court: String,
    val pitch_id: Any,
    val seleted_time_slots: Any,
    val skills: String,
    val start_timing_out: String,
    val total_players: String,
    val updated_at: String
)