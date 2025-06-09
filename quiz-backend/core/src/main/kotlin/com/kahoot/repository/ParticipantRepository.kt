package com.kahoot.repository

interface ParticipantRepository {
    /**
     * Add a participant to a quiz session, returning the new participantId.
     */
    fun add(quizId: String, participantName: String): String

    fun remove(quizId: String, participantId: String)

    fun count(quizId: String): Int                   // ← new

}
