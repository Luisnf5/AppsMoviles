package com.example.tragomaestro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        AchievementEntity::class,
        QuestionPackEntity::class,
        QuestionEntity::class,
        GameStatsEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class TragoMaestroDatabase : RoomDatabase() {

    abstract fun achievementDao(): AchievementDao
    abstract fun questionPackDao(): QuestionPackDao
    abstract fun questionDao(): QuestionDao
    abstract fun gameStatsDao(): GameStatsDao

    companion object {
        @Volatile
        private var INSTANCE: TragoMaestroDatabase? = null

        fun getDatabase(context: Context): TragoMaestroDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TragoMaestroDatabase::class.java,
                    "tragomaestro_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}