package com.kahoot.socket

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    @Primary
    fun redisTemplate(cf: RedisConnectionFactory): RedisTemplate<String, String> =
        RedisTemplate<String, String>().apply {
            connectionFactory = cf
            // ensure both keys *and* values are String-serialized
            keySerializer = StringRedisSerializer()
            valueSerializer = StringRedisSerializer()
            hashKeySerializer = StringRedisSerializer()
            hashValueSerializer = StringRedisSerializer()
        }

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory =
        LettuceConnectionFactory("localhost", 6379)

    @Bean
    fun redisListenerContainer(
        cf: RedisConnectionFactory,
        handler: QuizWebSocketHandler
    ): RedisMessageListenerContainer = RedisMessageListenerContainer().apply {
        connectionFactory = cf
        // log right before subscribing
        addMessageListener({ message, _ ->
            handler.handleRedisMessage(message)
        }, PatternTopic("quiz:*"))

        // Spring will call afterPropertiesSet() & start() for you
    }
}

