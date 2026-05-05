package com.example.tragomaestro.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface QuestionPackDao {

    @Query("SELECT * FROM question_packs ORDER BY isCustom ASC, name ASC")
    fun getAllPacks(): LiveData<List<QuestionPackEntity>>

    @Query("SELECT COUNT(*) FROM question_packs")
    suspend fun countPacks(): Int

    @Query("SELECT * FROM question_packs WHERE isSelected = 1")
    suspend fun getSelectedPacks(): List<QuestionPackEntity>

    @Insert
    suspend fun insertPack(pack: QuestionPackEntity): Long

    @Delete
    suspend fun deletePack(pack: QuestionPackEntity)

    @Query("UPDATE question_packs SET isSelected = :selected WHERE id = :packId")
    suspend fun setPackSelected(packId: Int, selected: Boolean)
}