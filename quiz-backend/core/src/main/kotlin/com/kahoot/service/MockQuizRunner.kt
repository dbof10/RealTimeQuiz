package com.kahoot.service

import com.kahoot.model.Question
import com.kahoot.repository.EventPublisher
import com.kahoot.repository.ParticipantRepository
import com.kahoot.repository.QuestionRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

@ConditionalOnProperty("mockquiz.enabled", havingValue = "true")
@Component
class MockQuizRunner(
    private val participantRepo: ParticipantRepository,
    private val questionRepo: QuestionRepository,
    private val redis: RedisTemplate<String, String>,
    private val events: EventPublisher
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    override fun run(vararg args: String) {
        val quizId = "123"
        val questions = questionRepo.getAll()
        var startTask: ScheduledFuture<*>? = null

        startTask = scheduler.scheduleAtFixedRate({
            val count = participantRepo.count(quizId)
            if (count >= 2) {
                logger.info("Quiz $quizId reached $count participants; starting in 5s")
                scheduler.schedule({ launchQuestionLoop(quizId, questions) }, 5, TimeUnit.SECONDS)
                startTask?.cancel(false)
            } else {
                logger.debug("Waiting for >=2 participants; currently $count")
            }
        }, 0, 1, TimeUnit.SECONDS)
    }

    private fun launchQuestionLoop(quizId: String, questions: List<Question>) {
        logger.info("=== Starting quiz $quizId ===")
        var index = 0

  scheduler.scheduleAtFixedRate({
            val q = questions[index++ % questions.size]
            val now = Instant.now()
            val end = now.plusSeconds(10)

            // Publish to all WS clients
            val payload = mapOf(
                "type" to "new_question",
                "payload" to mapOf(
                    "questionId" to q.questionId,
                    "text" to q.text,
                    "options" to q.options,
                    "startTime" to now.toString(),
                    "endTime" to end.toString()
                )
            )
            logger.info("Publishing question ${q.questionId} for quiz $quizId")
            events.publishRaw(quizId, payload)

            // Store correct answer and end time for answer validation
            redis.opsForValue().set("quiz:$quizId:question:${q.questionId}:correct", q.correctAnswer)
            redis.opsForValue().set("quiz:$quizId:question:${q.questionId}:endTime", end.toString())
        }, 0, 10, TimeUnit.SECONDS)
    }
}

