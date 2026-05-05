package com.example.tragomaestro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tragomaestro.repository.GameStatsRepository

class GameStatsViewModelFactory(
    private val repository: GameStatsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameStatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameStatsViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}