package com.example.tragomaestro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tragomaestro.database.QuestionEntity
import com.example.tragomaestro.database.QuestionPackEntity
import com.example.tragomaestro.repository.QuestionPackRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class QuestionPacksViewModel(
    private val repository: QuestionPackRepository
) : ViewModel() {

    val packs: LiveData<List<QuestionPackEntity>> =
        repository.packs

    init {
        viewModelScope.launch {
            repository.initializeDefaultPacksIfNeeded()
        }
    }

    fun setPackSelected(packId: Int, selected: Boolean) {
        viewModelScope.launch {
            repository.setPackSelected(packId, selected)
            Timber.i("Pack $packId seleccionado=$selected")
        }
    }

    fun createPack(name: String, description: String) {
        viewModelScope.launch {
            repository.createPack(
                name = name,
                description = description,
                isCustom = true
            )
            Timber.i("Pack creado: $name")
        }
    }

    fun addQuestion(
        packId: Int,
        text: String,
        optionA: String,
        optionB: String,
        optionC: String,
        optionD: String
    ) {
        viewModelScope.launch {
            repository.addQuestion(
                packId = packId,
                text = text,
                optionA = optionA,
                optionB = optionB,
                optionC = optionC,
                optionD = optionD
            )
            Timber.i("Pregunta añadida al pack $packId")
        }
    }

    fun getQuestionsByPack(packId: Int): LiveData<List<QuestionEntity>> {
        return repository.getQuestionsByPack(packId)
    }

    fun deletePack(pack: QuestionPackEntity) {
        viewModelScope.launch {
            repository.deletePack(pack)
        }
    }

    fun deleteQuestion(question: QuestionEntity) {
        viewModelScope.launch {
            repository.deleteQuestion(question)
        }
    }
}