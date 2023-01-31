package com.go.sport.model.leaderboardnew

data class LeaderboardModel(
    val `data`: List<LeaderboardData>,
    val first: PlayerFirst,
    val message: String,
    val second: PlayerSecond,
    val status: Boolean,
    val third: PlayerThird
)