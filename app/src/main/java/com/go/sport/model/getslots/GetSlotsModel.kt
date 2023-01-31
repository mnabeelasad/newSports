package com.go.sport.model.getslots

data class GetSlotsModel(
    val message: String,
    val slots: List<GetSlotsSlot>,
    val status: String,
    val total: Int
)