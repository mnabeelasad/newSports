package com.go.sport.model.getsports

data class GetSportsModel(
    val message: String,
    val status: Boolean,
    val sports: List<GetSportsSport>
)