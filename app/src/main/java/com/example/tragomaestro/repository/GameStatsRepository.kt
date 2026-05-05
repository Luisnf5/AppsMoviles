package com.example.tragomaestro.repository

import androidx.lifecycle.LiveData
import com.example.tragomaestro.database.GameStatsDao
import com.example.tragomaestro.database.GameStatsEntity
import timber.log.Timber

class GameStatsRepository(
    private val gameStatsDao: GameStatsDao
) {

    val stats: LiveData<GameStatsEntity?> =
        gameStatsDao.getStats()

    suspend fun initializeStatsIfNeeded() {
        gameStatsDao.insert(
            GameStatsEntity(
                id = 1,
                roundsPlayed = 0,
                correctGuesses = 0,
                failedGuesses = 0
            )
        )
        Timber.i("Estadísticas inicializadas")
    }

    suspend fun registerRoundCompleted() {
        gameStatsDao.incrementRounds()
    }

    suspend fun registerCorrectGuess() {
        gameStatsDao.incrementCorrect()
    }

    suspend fun registerFailedGuess() {
        gameStatsDao.incrementFailed()
    }
}