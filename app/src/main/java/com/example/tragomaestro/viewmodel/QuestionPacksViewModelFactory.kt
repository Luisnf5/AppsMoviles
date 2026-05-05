package com.example.tragomaestro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tragomaestro.repository.QuestionPackRepository

class QuestionPacksViewModelFactory(
    private val repository: QuestionPackRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionPacksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuestionPacksViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}