package com.kahoot.model

data class Question(
    val questionId: String,
    val text: String,
    val options: List<String>,
    val correctAnswer: String
)

