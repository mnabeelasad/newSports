package com.go.sport.model.fundrequest.received


import com.google.gson.annotations.SerializedName

data class ReceivedFundRequestsData(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("date_appended")
    val dateAppended: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("requested_by")
    val requestedBy: String,
    @SerializedName("requested_to")
    val requestedTo: String,
    @SerializedName("time_appended")
    val timeAppended: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user")
    val user: ReceivedFundRequestsUser
)