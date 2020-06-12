package io.github.volkanozkan.resilience4jdemo.resilience

import java.time.Duration

data class CircuitBreakerConfiguration(
    val failureRateThreshold: Float = 10F,
    val slidingWindowSize: Int = 2,
    val permittedNumberOfCallsInHalfOpenState: Int = 2,
    val waitDurationInOpenState: Duration = Duration.ofSeconds(60),
    val minimumNumberOfCalls: Int = 1
)