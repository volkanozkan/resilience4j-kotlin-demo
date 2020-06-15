package io.github.volkanozkan.resilience4jdemo.business.service

import io.github.volkanozkan.resilience4jdemo.resilience.CircuitBreakerConfiguration
import io.github.volkanozkan.resilience4jdemo.resilience.RateLimiterConfiguration
import io.github.volkanozkan.resilience4jdemo.resilience.ResiliencyHelper
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class Service(private val resiliencyHelper: ResiliencyHelper) {
    fun callWithRateLimiter(): String {
        return resiliencyHelper.runResiliently(
            name = "test-rl",
            rateLimiterConfiguration = RateLimiterConfiguration()
        ) {
            "OK"
        }
    }

    fun callWithCircuitBreaker(): String {
        return resiliencyHelper.runResiliently(
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