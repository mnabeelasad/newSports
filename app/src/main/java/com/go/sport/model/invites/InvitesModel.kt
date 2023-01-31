package com.go.sport.model.invites

data class InvitesModel(
    val Invites: List<InvitesInvite>,
    val message: String,
    val status: Boolean
)