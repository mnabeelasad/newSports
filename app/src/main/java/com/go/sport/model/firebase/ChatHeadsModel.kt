package com.go.sport.model.firebase

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatHeadsModel(
    val id: String = "",
    val date: String = "",
    val image: String = "",
    val group: Boolean = false,
    val name: String = "",
    val text: String = "",
    val blocked: String = "false",
    val read: Boolean = false
) : Parcelable