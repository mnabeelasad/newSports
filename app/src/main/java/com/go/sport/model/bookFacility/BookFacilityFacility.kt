package com.go.sport.model.bookFacility


import com.go.sport.model.getfeatures.GetFeaturesData

data class BookFacilityFacility(
    val address: String,
    val contract_tenure_end: Any,
    val contract_tenure_start: Any,
    val created_at: String,
    val created_by: String,
    val description: String,
    val feature: List<GetFeaturesData>,
    val id: Int,
    val image: String,
    val lat_lng: String,
    val name: String,
    val phone: String,
    val pitchsize: String,
    val pricing: String,
    val rating: String,
    val social_ids: String,
    val updated_at: String
)