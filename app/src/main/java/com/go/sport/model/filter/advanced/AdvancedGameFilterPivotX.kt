package com.go.sport.model.filter.advanced


import com.google.gson.annotations.SerializedName

data class AdvancedGameFilterPivotX(
    @SerializedName("game_id")
    val gameId: String,
    @SerializedName("timeslot_id")
    val timeslotId: String
)