package com.example.tragomaestro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tragomaestro.repository.AchievementRepository

class AchievementsViewModelFactory(
    private val repository: AchievementRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AchievementsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AchievementsViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}