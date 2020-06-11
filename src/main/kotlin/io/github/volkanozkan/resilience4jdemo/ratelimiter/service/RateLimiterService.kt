package io.github.volkanozkan.resilience4jdemo.ratelimiter.service

import io.github.volkanozkan.resilience4jdemo.resilience.RateLimiterConfiguration
import io.github.volkanozkan.resilience4jdemo.resilience.Resilience
import org.springframework.stereotype.Component

@Component
class RateLimiterService(private val resilience: Resilience) {
    fun call(): String {
        return resilience(
            name = "test",
            rateLimiterConfig = RateLimiterConfiguration.defaultRLConfiguration
        ) {
            "OK"
        }
    }
}