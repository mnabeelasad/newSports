package com.go.sport.model.bookFacility

data class BookFacilitySlot(
    val created_at: String,
    val date_event: String,
    val duration: Int,
    val end: String,
    val facility_id: String,
    val id: Int,
    val name: String,
    val payment_type: String,
    val pitchsize_id: String,
    val pivot: PivotX,
    val slot_price: String,
    val start: String,
    val status: String,
    val updated_at: String
)