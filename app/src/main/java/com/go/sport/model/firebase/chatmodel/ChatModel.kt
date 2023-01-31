package com.go.sport.model.firebase.chatmodel


import com.google.gson.annotations.SerializedName

data class ChatModel(
    @SerializedName("data")
    val `data`: ChatData = ChatData(""),
    @SerializedName("date")
    val date: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("sender")
    val sender: Sender = Sender("",""),
    @SerializedName("type")
    val type: String = ""
)