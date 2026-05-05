package com.example.tragomaestro.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_packs")
data class QuestionPackEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val isCustom: Boolean
)