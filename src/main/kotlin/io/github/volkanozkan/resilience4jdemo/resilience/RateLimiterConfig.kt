package io.github.volkanozkan.resilience4jdemo.resilience

import java.time.Duration

data class RateLimiterConfiguration(
    val limitForPeriod: Int = 2,
    val refreshPeriod: Duration = Duration.ofSeconds(1),
    val timeout: Duration = Duration.ofMillis(100)
)


