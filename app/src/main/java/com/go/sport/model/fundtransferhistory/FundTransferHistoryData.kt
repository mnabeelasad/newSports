package com.go.sport.model.fundtransferhistory


import com.google.gson.annotations.SerializedName

data class FundTransferHistoryData(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("credited_by")
    val creditedBy: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("long_description")
    val longDescription: String,
    @SerializedName("reference_number")
    val referenceNumber: String,
    @SerializedName("short_description")
    val shortDescription: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_wallet_id")
    val userWalletId: String,
    @SerializedName("date_appended")
    val dateAppended: String,
    @SerializedName("time_appended")
    val TimeAppended: String,
)