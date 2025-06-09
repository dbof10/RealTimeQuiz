package com.kahoot.model

data class AnswerRequest(
    val participantId: String,
    val questionId: String,
    val selectedOption: String,
    val answeredAt: String
)
