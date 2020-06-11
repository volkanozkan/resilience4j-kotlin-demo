package io.github.volkanozkan.resilience4jdemo.resilience

import java.time.Duration

data class RateLimiterConfiguration(val limitForPeriod: Int, val refreshPeriod: Duration, val timeout: Duration) {
    companion object {
        @JvmField
        val defaultRLConfiguration = RateLimiterConfiguration(
            limitForPeriod = 2,
            refreshPeriod = Duration.ofSeconds(1),
            timeout = Duration.ofMillis(100)
        )
    }
}


