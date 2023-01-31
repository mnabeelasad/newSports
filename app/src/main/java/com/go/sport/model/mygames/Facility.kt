package com.go.sport.model.mygames


import com.go.sport.model.getfeatures.GetFeaturesData
import com.google.gson.annotations.SerializedName

data class Facility(
    @SerializedName("address")
    val address: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("feature")
    val feature: List<GetFeaturesData>,
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
    val updatedAt: String,
    @SerializedName("lat_lng")
    val lat_lng: String

)