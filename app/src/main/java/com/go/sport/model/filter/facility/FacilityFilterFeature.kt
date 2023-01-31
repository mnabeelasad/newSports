package com.go.sport.model.filter.facility


import com.google.gson.annotations.SerializedName

data class FacilityFilterFeature(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("pivot")
    val pivot: FacilityFilterPivot,
    @SerializedName("status")
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String
)