package com.go.sport.model.filter.game


import com.google.gson.annotations.SerializedName

data class GameFilterPivotX(
    @SerializedName("game_id")
    val gameId: String,
    @SerializedName("timeslot_id")
    val timeslotId: String
)