package com.example.tragomaestro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tragomaestro.repository.AchievementRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class AchievementsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AchievementRepository(application)

    val achievements = repository.achievements

    init {
        viewModelScope.launch {
            Timber.i("Inicializando logros desde AchievementsViewModel")
            repository.initializeAchievementsIfNeeded()
        }
    }
}