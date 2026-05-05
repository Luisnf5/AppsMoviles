package com.example.tragomaestro.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val progress: Int,
    val target: Int,
    val unlocked: Boolean
)