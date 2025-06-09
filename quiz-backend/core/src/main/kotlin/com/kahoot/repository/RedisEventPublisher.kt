// RedisEventPublisher.kt
package com.kahoot.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.kahoot.model.ParticipantScore
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class RedisEventPublisher(
    private val redis: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) : EventPublisher {

    override fun publishLeaderboardChanged(quizId: String, top: List<ParticipantScore>) {
        val msg = mapOf("type" to "leaderboard_changed", "payload" to top)
        val json = objectMapper.writeValueAsString(msg)
        redis.convertAndSend("quiz:$quizId", json)
    }

    override fun publishRaw(quizId: String, message: Any) {
        // If you still need raw for testing, ensure it's a String
        val json = if (message is String) message else objectMapper.writeValueAsString(message)
        redis.convertAndSend("quiz:$quizId", json)
    }
}
