package com.go.sport.model.invites

data class InvitesFacility(
    val address: String,
    val created_at: String,
    val description: String,
    val feature: List<InvitesFeature>,
    val id: Int,
    val image: String,
    val name: String,
    val phone: String,
    val pitchsize: String,
    val pricing: String,
    val rating: String,
    val updated_at: String
)