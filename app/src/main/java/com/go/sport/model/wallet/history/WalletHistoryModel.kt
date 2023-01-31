package com.go.sport.model.wallet.history

data class WalletHistoryModel(
    val `data`: List<WalletHistoryData>,
    val message: String,
    val status: Boolean
)