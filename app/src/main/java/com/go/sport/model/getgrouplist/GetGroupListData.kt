package com.go.sport.model.getgrouplist


import com.google.gson.annotations.SerializedName

data class GetGroupListData(
    @SerializedName("action")
    val action: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("created_by")
    val createdBy: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("members")
    val members: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("owner_name")
    val ownerName: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String
)