package com.go.sport.model.academies


import com.google.gson.annotations.SerializedName

data class AcademiesModel(
    @SerializedName("Acadmies")
    val acadmies: List<AcademiesAcadmy>? = emptyList(),
    @SerializedName("Academy")
    val academy: List<AcademiesAcadmy>? = emptyList(),
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)