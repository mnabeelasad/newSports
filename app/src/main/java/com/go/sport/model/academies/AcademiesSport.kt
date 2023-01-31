package com.go.sport.model.academies


import com.google.gson.annotations.SerializedName

data class AcademiesSport(
    @SerializedName("court_type")
    val courtType: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String
)