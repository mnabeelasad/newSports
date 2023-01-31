package com.go.sport.model.getslots

data class GetSlotsSlot(
    val created_at: String,
    val date_event: String,
    val duration: String,
    val end: String,
    val facility_id: String,
    val id: String,
    val name: String,
    val payment_type: String,
    val pitchsize_id: String,
    val slot_price: String,
    val start: String,
    val status: Int,
    val updated_at: String,
    var isSelected: Boolean = false
)