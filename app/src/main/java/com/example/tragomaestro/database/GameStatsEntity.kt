package com.example.tragomaestro.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_stats")
data class GameStatsEntity(
    @PrimaryKey val id: Int = 1,
    val roundsPlayed: Int,
    val correctGuesses: Int,
    val failedGuesses: Int
)