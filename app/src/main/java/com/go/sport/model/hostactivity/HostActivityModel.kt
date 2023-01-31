package com.go.sport.model.hostactivity

import com.go.sport.model.hostactivity.hostForLocation.HostActivityForLocationGame

data class HostActivityModel(
    val game: HostActivityForLocationGame,
    val message: String,
    val status: Boolean
)