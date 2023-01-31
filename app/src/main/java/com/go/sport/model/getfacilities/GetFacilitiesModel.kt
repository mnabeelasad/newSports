package com.go.sport.model.getfacilities

data class GetFacilitiesModel(
    val data: List<GetFacilitiesData>? = emptyList(),
    val facilities: List<GetFacilitiesData>? = emptyList(),
    val message: String,
    val status: Boolean
)