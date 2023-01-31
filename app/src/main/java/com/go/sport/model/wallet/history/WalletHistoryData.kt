package com.go.sport.model.wallet.history

data class WalletHistoryData(
    val amount: String,
    val created_at: String,
    val credited_by: String,
    val id: Int,
    val long_description: String,
    val reference_number: String,
    val short_description: String,
    val type: String,
    val updated_at: String,
    val user_wallet_id: String,
    val date_appended: String,
    val time_appended: String
)