package com.example.tragomaestro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tragomaestro.database.AchievementEntity
import com.example.tragomaestro.repository.AchievementRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class AchievementsViewModel(
    private val repository: AchievementRepository
) : ViewModel() {

    val achievements: LiveData<List<AchievementEntity>> =
        repository.achievements

    fun initialize(locale: String) {
        viewModelScope.launch {
            Timber.i("Inicializando logros")
            repository.initializeAchievementsIfNeeded(locale)
        }
    }

    fun registerRoundCompleted() {
        viewModelScope.launch {
            repository.registerRoundCompleted()
        }
    }

    fun registerPlayersCount(count: Int) {
        viewModelScope.launch {
            repository.registerPlayersCount(count)
        }
    }

    fun registerCorrectGuess() {
        viewModelScope.launch {
            repository.registerCorrectGuess()
        }
    }

    fun registerFailedGuess() {
        viewModelScope.launch {
            repository.registerFailedGuess()
        }
    }
}