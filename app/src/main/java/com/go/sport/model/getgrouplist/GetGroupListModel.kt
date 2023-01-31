package com.go.sport.model.getgrouplist


import com.google.gson.annotations.SerializedName

data class GetGroupListModel(
    @SerializedName("data")
    val `data`: ArrayList<GetGroupListData>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: String
)