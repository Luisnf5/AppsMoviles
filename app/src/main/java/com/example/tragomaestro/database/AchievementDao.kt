package com.example.tragomaestro.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AchievementDao {

    @Query("SELECT * FROM achievements")
    fun observeAchievements(): LiveData<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(achievements: List<AchievementEntity>)

    @Query("UPDATE achievements SET progress = progress + 1 WHERE id = :id")
    suspend fun incrementProgress(id: String)

    @Query("UPDATE achievements SET unlocked = 1 WHERE id = :id")
    suspend fun unlockAchievement(id: String)

    @Query("SELECT COUNT(*) FROM achievements")
    suspend fun countAchievements(): Int
}