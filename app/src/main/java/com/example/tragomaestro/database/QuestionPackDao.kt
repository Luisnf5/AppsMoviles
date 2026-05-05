package com.example.tragomaestro.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface QuestionPackDao {

    @Query("SELECT * FROM question_packs")
    fun getAllPacks(): LiveData<List<QuestionPackEntity>>

    @Insert
    suspend fun insertPack(pack: QuestionPackEntity): Long

    @Delete
    suspend fun deletePack(pack: QuestionPackEntity)
}