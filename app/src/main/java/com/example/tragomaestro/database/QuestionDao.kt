package com.example.tragomaestro.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface QuestionDao {

    @Query("SELECT * FROM questions WHERE packId = :packId")
    fun getQuestionsByPack(packId: Int): LiveData<List<QuestionEntity>>

    @Query("SELECT * FROM questions")
    suspend fun getAllQuestions(): List<QuestionEntity>

    @Query("""
        SELECT questions.* FROM questions
        INNER JOIN question_packs ON questions.packId = question_packs.id
        WHERE question_packs.isSelected = 1
    """)
    suspend fun getQuestionsFromSelectedPacks(): List<QuestionEntity>

    @Insert
    suspend fun insertQuestion(question: QuestionEntity)

    @Insert
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Delete
    suspend fun deleteQuestion(question: QuestionEntity)
}