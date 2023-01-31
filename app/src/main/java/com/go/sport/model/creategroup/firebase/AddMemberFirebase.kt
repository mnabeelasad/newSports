package com.go.sport.model.creategroup.firebase

import com.google.gson.annotations.SerializedName


data class AddMemberFirebase(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("name")
    val name: String = ""
)