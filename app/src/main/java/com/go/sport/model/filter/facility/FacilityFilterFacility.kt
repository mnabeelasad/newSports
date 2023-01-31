package com.go.sport.model.filter.facility


import com.google.gson.annotations.SerializedName

data class FacilityFilterFacility(
    @SerializedName("address")
    val address: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("feature")
    val feature: List<FacilityFilterFeature>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("pitchsize")
    val pitchsize: String,
    @SerializedName("pricing")
    val pricing: String,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("updated_at")
    val updatedAt: String
)