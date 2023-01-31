package com.go.sport.model.unjoinedgroups

data class ListUnjoinedModel(
    val `data`: List<ListUnjoinedData>,
    val message: String,
    val status: Boolean
)