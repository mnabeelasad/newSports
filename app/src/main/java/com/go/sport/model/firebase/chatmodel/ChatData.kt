package com.go.sport.model.firebase.chatmodel


import com.google.gson.annotations.SerializedName

data class ChatData(
    @SerializedName("text")
    val text: String = ""
)