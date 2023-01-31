package com.go.sport.model.getfacilitydata


import com.go.sport.model.getfeatures.GetFeaturesData
import com.google.gson.annotations.SerializedName

data class GetFacilityDataFacility(
    @SerializedName("address")
    val address: String,
    @SerializedName("contract_tenure_end")
    val contractTenureEnd: Any,
    @SerializedName("contract_tenure_start")
    val contractTenureStart: Any,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_by")
    val createdBy: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("facility_sports")
    val facilitySports: List<GetFacilityDataSport>,
    @SerializedName("feature")
    val feature: List<GetFeaturesData>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("lat_lng")
    val latLng: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("pitchsize")
    val pitchsize: List<GetFacilityDataPitchsize>,
    @SerializedName("pricing")
    val pricing: String,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("social_ids")
    val socialIds: String,
    @SerializedName("timeslot")
    val timeslot: List<GetFacilityDataTimeslot>,
    @SerializedName("updated_at")
    val updatedAt: String
)