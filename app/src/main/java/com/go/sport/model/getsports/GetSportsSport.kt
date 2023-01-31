package com.go.sport.model.getsports

data class GetSportsSport(
    val id: Int = 0,
    val name: String = "",
    val image: String = "",
    var isSelected: Boolean = false
)