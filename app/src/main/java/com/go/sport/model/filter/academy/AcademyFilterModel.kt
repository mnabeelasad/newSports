package com.go.sport.model.filter.academy


import com.google.gson.annotations.SerializedName

data class AcademyFilterModel(
    @SerializedName("Academy")
    val academy: List<AcademyFilterAcademy>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)