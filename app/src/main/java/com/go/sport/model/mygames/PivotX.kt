package com.go.sport.model.mygames


import com.google.gson.annotations.SerializedName

data class PivotX(
    @SerializedName("game_id")
    val gameId: String,
    @SerializedName("timeslot_id")
    val timeslotId: String
)