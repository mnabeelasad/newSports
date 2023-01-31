package com.go.sport.model.getfeatures

data class GetFeaturesModel(
    val `data`: List<GetFeaturesData>,
    val message: String,
    val status: Boolean
)