package com.kahoot.repository

import com.kahoot.model.ParticipantScore

/**
 * Abstraction for publishing real-time events (via Redis, WebSocket, etc.).
 */
interface EventPublisher {
    fun publishLeaderboardChanged(quizId: String, top: List<ParticipantScore>)
    fun publishRaw(quizId: String, message: Any)

}
