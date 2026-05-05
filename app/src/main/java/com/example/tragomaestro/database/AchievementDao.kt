package com.example.tragomaestro.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AchievementDao {

    @Query("SELECT * FROM achievements ORDER BY unlocked DESC, id ASC")
    fun observeAchievements(): LiveData<List<AchievementEntity>>

    @Query("SELECT COUNT(*) FROM achievements")
    suspend fun countAchievements(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(achievements: List<AchievementEntity>)

    @Query("""
        UPDATE achievements
        SET progress = CASE 
            WHEN progress + :amount >= target THEN target
            ELSE progress + :amount
        END,
        unlocked = CASE
            WHEN progress + :amount >= target THEN 1
            ELSE unlocked
        END
        WHERE id = :id
    """)
    suspend fun incrementProgress(id: String, amount: Int = 1)

    @Query("""
        UPDATE achievements
        SET progress = target,
            unlocked = 1
        WHERE id = :id
    """)
    suspend fun unlockAchievement(id: String)
}