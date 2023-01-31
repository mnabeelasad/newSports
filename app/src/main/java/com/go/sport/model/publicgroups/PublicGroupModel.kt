package com.go.sport.model.publicgroups

data class PublicGroupModel(
    val `data`: List<PublicGroupData>,
    val message: String,
    val status: Boolean
)