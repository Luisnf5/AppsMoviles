package com.example.tragomaestro.model

data class Question(
    val id: Int,
    val text: String,
    val answers: List<AnswerOption>
)