package com.example.tragomaestro.model

data class AnswerOption(
    val text: String,
    val style: AnswerStyle
)

enum class AnswerStyle {
    PINK,
    ORANGE,
    BLUE,
    PURPLE
}