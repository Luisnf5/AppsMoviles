package com.example.tragomaestro.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions WHERE packId = :packId")
    fun getQuestionsByPack(packId: Int): LiveData<List<QuestionEntity>>

    @Insert
    suspend fun insertQuestion(question: QuestionEntity)

    @Delete
    suspend fun deleteQuestion(question: QuestionEntity)
}