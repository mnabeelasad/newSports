package com.go.sport.model.firebase.chatmodel


import com.google.gson.annotations.SerializedName

data class Sender(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = ""
)