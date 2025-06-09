package com.kahoot.repository

import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class RedisParticipantRepository(
    redisTemplate: RedisTemplate<String, String>
) : ParticipantRepository {
    private val hashOps: HashOperations<String, String, String> = redisTemplate.opsForHash()
    private val zsetOps: ZSetOperations<String, String> = redisTemplate.opsForZSet()

    override fun add(quizId: String, participantName: String): String {
        val pid = UUID.randomUUID().toString()
        hashOps.put("quiz:$quizId:participants", pid, participantName)
        zsetOps.add("quiz:$quizId:scores", pid, 0.0)
        return pid
    }


    override fun remove(quizId: String, participantId: String) {
        hashOps.delete("quiz:$quizId:participants", participantId)
    }

    override fun count(quizId: String): Int =
        hashOps.size("quiz:$quizId:participants").toInt()
}
