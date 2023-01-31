package com.go.sport.model.play

data class PlayData(
    val complete_time: String,
    val created_at: String,
    val date: String,
    val detail: PlayDetail,
    val duration: String,
    val facility: PlayFacility,
    val facility_id: String,
    val facility_title: String,
    val id: Int,
    val image: String,
    val join_count: String,
    val joined: Boolean? = false,
    val slot: List<PlaySlot>,
    val sport: PlaySport,
    val sports_id: String,
    val title: String,
    val updated_at: String,
    val user: PlayUser,
    val user_id: String,
    val status: String,
    val type: String,
    val currency : String,

)