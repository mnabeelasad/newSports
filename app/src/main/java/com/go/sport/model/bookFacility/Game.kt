package com.go.sport.model.bookFacility

data class Game(
    val booking_id: String,
    val created_at: String,
    val currency: String,
    val date: String,
    val detail: BookFacilityDetail,
    val facility: BookFacilityFacility,
    val facility_id: String,
    val facility_title: Any,
    val id: Int,
    val image: String,
    val join_count: String,
    val lat_lng: String,
    val slot: List<BookFacilitySlot>,
    val sport: Sport,
    val sports_id: String,
    val status: String,
    val title: String,
    val type: String,
    val updated_at: String,
    val user: User,
    val user_id: String
)