package com.kahoot.repository

import com.kahoot.model.ParticipantScore

interface ScoreRepository {
    /**
     * Increment the participant's score by delta.
     */
    fun updateScore(quizId: String, participantId: String, delta: Int)

    /**
     * Return the current total score for a participant.
     */
    fun getScore(quizId: String, participantId: String): Int

    /**
     * Return the participant's rank (1-based) within their quiz.
     */
    fun getRank(quizId: String, participantId: String): Int

    /**
     * Retrieve the top N participants with their scores and ranks.
     */
    fun topN(quizId: String, n: Int): List<ParticipantScore>

    fun remove(quizId: String, participantId: String)
}
