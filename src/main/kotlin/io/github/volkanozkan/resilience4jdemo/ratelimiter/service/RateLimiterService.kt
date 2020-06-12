package io.github.volkanozkan.resilience4jdemo.ratelimiter.service

import io.github.volkanozkan.resilience4jdemo.resilience.CircuitBreakerConfiguration
import io.github.volkanozkan.resilience4jdemo.resilience.RateLimiterConfiguration
import io.github.volkanozkan.resilience4jdemo.resilience.Resilience
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class RateLimiterService(private val resilience: Resilience) {
    fun callWithRateLimiter(): String {
        return resilience(
            name = "test-rl",
            rateLimiterConfiguration = RateLimiterConfiguration()
        ) {
            "OK"
        }
    }

    fun callWithCircuitBreaker(): String {
        return resilience(
            name = "test-cb",
            circuitBreakerConfiguration = CircuitBreakerConfiguration()
        ) {
            if (Random.nextBoolean()) {
                throw IllegalArgumentException("Some error")
            }
            "OK"
        }
    }
}