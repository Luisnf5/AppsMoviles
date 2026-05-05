package com.example.tragomaestro.repository

import androidx.lifecycle.LiveData
import com.example.tragomaestro.database.QuestionDao
import com.example.tragomaestro.database.QuestionEntity
import com.example.tragomaestro.database.QuestionPackDao
import com.example.tragomaestro.database.QuestionPackEntity
import timber.log.Timber

class QuestionPackRepository(
    private val packDao: QuestionPackDao,
    private val questionDao: QuestionDao
) {

    val packs: LiveData<List<QuestionPackEntity>> =
        packDao.getAllPacks()

    suspend fun createPack(name: String, isCustom: Boolean = true): Long {
        return packDao.insertPack(
            QuestionPackEntity(
                name = name,
                isCustom = isCustom
            )
        )
    }

    suspend fun addQuestion(
        packId: Int,
        text: String,
        optionA: String,
        optionB: String,
        optionC: String,
        optionD: String
    ) {
        questionDao.insertQuestion(
            QuestionEntity(
                packId = packId,
                text = text,
                optionA = optionA,
                optionB = optionB,
                optionC = optionC,
                optionD = optionD
            )
        )
    }

    fun getQuestionsByPack(packId: Int): LiveData<List<QuestionEntity>> {
        return questionDao.getQuestionsByPack(packId)
    }

    suspend fun deletePack(pack: QuestionPackEntity) {
        packDao.deletePack(pack)
    }

    suspend fun deleteQuestion(question: QuestionEntity) {
        questionDao.deleteQuestion(question)
    }

    suspend fun initializeDefaultPacksIfNeeded() {
        Timber.i("Inicialización de packs preparada")
        // Más adelante aquí meteremos packs iniciales si hace falta.
    }
}