package com.go.sport.model.filter.game


import com.google.gson.annotations.SerializedName

data class GameFilterFeature(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("pivot")
    val pivot: GameFilterPivot,
    @SerializedName("status")
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String
)