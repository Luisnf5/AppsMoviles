package com.example.tragomaestro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tragomaestro.database.GameStatsEntity
import com.example.tragomaestro.repository.GameStatsRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class GameStatsViewModel(
    private val repository: GameStatsRepository
) : ViewModel() {

    val stats: LiveData<GameStatsEntity?> =
        repository.stats

    init {
        viewModelScope.launch {
            repository.initializeStatsIfNeeded()
        }
    }

    fun registerRoundCompleted() {
        viewModelScope.launch {
            repository.registerRoundCompleted()
            Timber.i("Ronda registrada en estadísticas")
        }
    }

    fun registerCorrectGuess() {
        viewModelScope.launch {
            repository.registerCorrectGuess()
            Timber.i("Acierto registrado en estadísticas")
        }
    }

    fun registerFailedGuess() {
        viewModelScope.launch {
            repository.registerFailedGuess()
            Timber.i("Fallo registrado en estadísticas")
        }
    }
}