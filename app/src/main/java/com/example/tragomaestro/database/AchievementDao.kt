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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(achievements: List<AchievementEntity>)

    @Query("""
        UPDATE achievements
        SET progress = CASE
            WHEN progress + 1 >= target THEN target
            ELSE progress + 1
        END,
        unlocked = CASE
            WHEN progress + 1 >= target THEN 1
            ELSE unlocked
        END
        WHERE id = :id
    """)
    suspend fun incrementProgress(id: String)

    @Query("""
        UPDATE achievements
        SET progress = target,
            unlocked = 1
        WHERE id = :id
    """)
    suspend fun unlockAchievement(id: String)

    @Query("SELECT COUNT(*) FROM achievements")
    suspend fun countAchievements(): Int
}