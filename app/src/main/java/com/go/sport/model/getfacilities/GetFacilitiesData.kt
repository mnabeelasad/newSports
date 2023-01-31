package com.go.sport.model.getfacilities


import com.go.sport.model.getfeatures.GetFeaturesData

data class GetFacilitiesData(
    val address: String,
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val phone: String,
    val pitchsize: String,
    val pricing: String,
    val rating: String,
    val feature: ArrayList<GetFeaturesData>,
    val updated_at: String,
    val facility_sports: ArrayList<GetFacilitiesFeatures>,
    val lat_lng : String
)