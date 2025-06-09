package com.kahoot.model

data class JoinRequest(val participantName: String)

data class JoinResponse(val quizId: String, val participantId: String)



