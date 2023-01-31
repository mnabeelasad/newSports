package com.go.sport.model.mybookings


import com.go.sport.model.mygames.MyGames
import com.google.gson.annotations.SerializedName

data class MyBookingsModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("my_bookings")
    val myBookings: MyGames,
    @SerializedName("status")
    val status: Boolean
)