package com.go.sport.model.invites

data class InvitesInvite(
    val booking_id: String,
    val complete_time: String,
    val created_at: String,
    val currency: String,
    val date: String,
    val detail: InvitesDetail,
    val duration: String,
    val facility: InvitesFacility,
    val facility_id: String,
    val facility_title: String,
    val id: Int,
    val image: String,
    val invite: InvitesInviteX,
    val join_count: String,
    val lat_lng: String,
    val slot: List<Any>,
    val sport: InvitesSport,
    val sports_id: String,
    val status: String,
    val title: String,
    val type: String,
    val updated_at: String,
    val user: InvitesUser,
    val user_id: String
)