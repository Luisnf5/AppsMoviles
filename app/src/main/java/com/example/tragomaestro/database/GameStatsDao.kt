package com.example.tragomaestro.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GameStatsDao {

    @Query("SELECT * FROM game_stats WHERE id = 1")
    fun getStats(): LiveData<GameStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stats: GameStatsEntity)

    @Query("UPDATE game_stats SET roundsPlayed = roundsPlayed + 1 WHERE id = 1")
    suspend fun incrementRounds()

    @Query("UPDATE game_stats SET correctGuesses = correctGuesses + 1 WHERE id = 1")
    suspend fun incrementCorrect()

    @Query("UPDATE game_stats SET failedGuesses = failedGuesses + 1 WHERE id = 1")
    suspend fun incrementFailed()
}