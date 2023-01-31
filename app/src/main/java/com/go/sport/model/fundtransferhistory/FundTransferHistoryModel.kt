package com.go.sport.model.fundtransferhistory


data class FundTransferHistoryModel(
    val `data`: List<FundTransferHistoryData>,
    val end: String,
    val message: String,
    val recieve: String,
    val sent: String,
    val start: String,
    val status: String
)