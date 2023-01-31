package com.go.sport.model.filter.academy


import com.google.gson.annotations.SerializedName

data class AcademyFilterAcademy(
    @SerializedName("address")
    val address: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("facebook")
    val facebook: String,
    @SerializedName("google")
    val google: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("social_links")
    val socialLinks: List<String>,
    @SerializedName("sport")
    val sport: List<AcademyFilterSport>,
    @SerializedName("timings")
    val timings: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("watsapp")
    val watsapp: String
)