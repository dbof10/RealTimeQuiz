package com.kahoot.socket

import com.fasterxml.jackson.databind.ObjectMapper
import com.kahoot.repository.ParticipantRepository
import com.kahoot.repository.ScoreRepository
import com.kahoot.service.QuizService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.net.URI
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

/**
 * Handles quiz WebSocket connections, cleans up on disconnect,
 * and broadcasts real-time events via Redis Pub/Sub.
 */
@Component
class QuizWebSocketHandler(
    private val objectMapper: ObjectMapper,
    private val quizService: QuizService,
    private val scoreRepository: ScoreRepository,
    private val participantRepository: ParticipantRepository
) : TextWebSocketHandler() {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val sessions = ConcurrentHashMap<String, WebSocketSession>()
    private val sessionInfo = ConcurrentHashMap<String, Pair<String, String>>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("WebSocket connected: sessionId=${session.id}, uri=${session.uri}")
        val params = parseQueryParams(session.uri.toString())
        val quizId = params["quizId"]
        val participantId = params["participantId"]
        if (quizId.isNullOrBlank() || participantId.isNullOrBlank()) {
            logger.warn("Missing quizId or participantId, closing session=${session.id}")
            session.close()
            return
        }
        sessions[session.id] = session
        sessionInfo[session.id] = quizId to participantId
        logger.debug("Registered session ${session.id} for quiz $quizId as participant $participantId")

        val topList = scoreRepository.topN(quizId, 10)
        logger.debug("Sending initial leaderboard for quiz {}: {}", quizId, topList)
        val snapshot = mapOf("type" to "leaderboard_data", "payload" to topList)
        session.sendMessage(TextMessage(objectMapper.writeValueAsString(snapshot)))
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info("Received WS message from session=${session.id}: ${message.payload}")
        val payload = objectMapper.readTree(message.payload)
        val type = payload.get("type").asText()
        val info = sessionInfo[session.id]
        if (info == null) {
            logger.error("No sessionInfo for session=${session.id}")
            return
        }
        val (quizId, _) = info

        when (type) {
            "answer_submission" -> {
                val p = payload.get("payload")
                val participantId = p.get("participantId").asText()
                val questionId = p.get("questionId").asText()
                val selectedOption = p.get("selectedOption").asText()
                val answeredAt = Instant.parse(p.get("answeredAt").asText())
                logger.debug(
                    "Submit answer quiz={}, participant={}, question={}, option={}, at={}",
                    quizId,
                    participantId,
                    questionId,
                    selectedOption,
                    answeredAt
                )
                quizService.submitAnswer(quizId, participantId, questionId, selectedOption, answeredAt)
            }
            "get_leaderboard" -> {
                val topList = scoreRepository.topN(quizId, 10)
                val response = mapOf("type" to "leaderboard_data", "payload" to topList)
                session.sendMessage(TextMessage(objectMapper.writeValueAsString(response)))
            }
            else -> {
                logger.warn("Unknown message type=$type from session=${session.id}")
                val err = mapOf("type" to "error", "payload" to mapOf("reason" to "Unknown type: $type"))
                session.sendMessage(TextMessage(objectMapper.writeValueAsString(err)))
            }
        }
    }

    fun handleRedisMessage(message: org.springframework.data.redis.connection.Message) {
        val body = message.body.toString(Charsets.UTF_8)
        val channel = message.channel.toString(Charsets.UTF_8)
        val quizId = channel.removePrefix("quiz:")
        sessions.values
            .filter { sessionInfo[it.id]?.first == quizId }
            .forEach {
                logger.trace("Forwarding to session=${it.id}")
                it.sendMessage(TextMessage(body))
            }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val info = sessionInfo.remove(session.id)
        sessions.remove(session.id)
        if (info != null) {
            val (quizId, participantId) = info
            logger.info("Session ${session.id} closed; removing participant $participantId from quiz $quizId")

            participantRepository.remove(quizId, participantId)
            scoreRepository.remove(quizId, participantId)

            val updatedTop = scoreRepository.topN(quizId, 10)
            logger.debug("Broadcasting updated leaderboard after removal: {}", updatedTop)
            quizService.publishLeaderboardSnapshot(quizId, updatedTop)
        }
    }

    private fun parseQueryParams(uri: String): Map<String, String> =
        URI(uri).query
            ?.split('&')
            ?.mapNotNull {
                val parts = it.split('=')
                if (parts.size == 2) parts[0] to parts[1] else null
            }
            ?.toMap()
            ?: emptyMap()
}
