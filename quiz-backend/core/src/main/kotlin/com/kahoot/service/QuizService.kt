package com.kahoot.service

import com.kahoot.model.ParticipantScore
import com.kahoot.repository.EventPublisher
import com.kahoot.repository.ParticipantRepository
import com.kahoot.repository.ScoreRepository
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class QuizService(
    private val participants: ParticipantRepository,
    private val scoreRepository: ScoreRepository,
    private val events: EventPublisher,
    private val redis: RedisTemplate<String, String>,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Registers a new participant in the quiz and returns their unique ID.
     */
    fun joinQuiz(quizId: String, name: String): String {
        val id = participants.add(quizId, name)
        val updatedTop = scoreRepository.topN(quizId, 10)
        events.publishLeaderboardChanged(quizId, updatedTop)
        return id
    }

    /**
     * Processes an answer submission: calculates score, updates ranking, and publishes events.
     */
    fun submitAnswer(
        quizId: String,
        participantId: String,
        questionId: String,
        selectedOption: String,
        answeredAt: Instant
    ) {
        val correctAnswerKey = "quiz:$quizId:question:$questionId:correct"
        val endTimeKey = "quiz:$quizId:question:$questionId:endTime"
        val answeredSetKey = "quiz:$quizId:answered:$questionId"

        val alreadyAnswered = redis.opsForSet().isMember(answeredSetKey, participantId) == true
        if (alreadyAnswered) {
            logger.warn("Duplicate submission: participant=$participantId already answered $questionId")
            return
        }

        val correctAnswer = redis.opsForValue().get(correctAnswerKey)
        val endTimeStr = redis.opsForValue().get(endTimeKey)

        if (correctAnswer == null || endTimeStr == null) {
            logger.warn("Missing correctAnswer or endTime for $quizId/$questionId")
            return
        }

        val isCorrect = selectedOption == correctAnswer
        val isInTime = answeredAt.isBefore(Instant.parse(endTimeStr))
        val points = if (isCorrect && isInTime) 10 else 0

        logger.info(
            "Answer submitted: quiz=$quizId participant=$participantId question=$questionId " +
                    "selected=$selectedOption correct=$correctAnswer → points=$points"
        )

        redis.opsForSet().add(answeredSetKey, participantId)

        if (points > 0) {
            scoreRepository.updateScore(quizId, participantId, points)
        }

        val topList = scoreRepository.topN(quizId, 10)
        events.publishLeaderboardChanged(quizId, topList)
    }

    fun publishLeaderboardSnapshot(quizId: String, topList: List<ParticipantScore>) {
        events.publishLeaderboardChanged(quizId, topList)
    }
}
