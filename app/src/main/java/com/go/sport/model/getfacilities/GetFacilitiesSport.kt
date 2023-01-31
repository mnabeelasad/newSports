package com.go.sport.model.getfacilities



data class GetFacilitiesSport(
    val courtType: String,
    val id: Int,
    val image: String,
    val name: String,
    var isSelected : Boolean= false
)