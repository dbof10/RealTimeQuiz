package com.kahoot.repository

import com.kahoot.model.ParticipantScore
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Repository

@Repository
class RedisScoreRepository(
    private val redisTemplate: RedisTemplate<String, String>
) : ScoreRepository {
    private val zsetOps: ZSetOperations<String, String> = redisTemplate.opsForZSet()
    private val hashOps: HashOperations<String, String, String> = redisTemplate.opsForHash()

    override fun updateScore(quizId: String, participantId: String, delta: Int) {
        zsetOps.incrementScore("quiz:$quizId:scores", participantId, delta.toDouble())
    }

    override fun getScore(quizId: String, participantId: String): Int {
        return zsetOps.score("quiz:$quizId:scores", participantId)?.toInt() ?: 0
    }

    override fun getRank(quizId: String, participantId: String): Int {
        return zsetOps.reverseRank("quiz:$quizId:scores", participantId)?.toInt()?.plus(1) ?: -1
    }

    override fun topN(quizId: String, n: Int): List<ParticipantScore> {
        val tuples = zsetOps
            .reverseRangeWithScores("quiz:$quizId:scores", 0, (n - 1).toLong())

        val names = hashOps
            .multiGet("quiz:$quizId:participants", tuples.map { it.value }.toSet())
            .mapIndexed { idx, name -> tuples.elementAt(idx).value to (name ?: "") }
            .toMap()

        return tuples.mapIndexed { idx, t ->
            ParticipantScore(
                participantId = t.value,
                name = names[t.value] ?: "",
                score = t.score.toInt(),
                rank = idx + 1
            )
        }
    }

    override fun remove(quizId: String, participantId: String) {
        zsetOps.remove("quiz:$quizId:scores", participantId)
    }
}
