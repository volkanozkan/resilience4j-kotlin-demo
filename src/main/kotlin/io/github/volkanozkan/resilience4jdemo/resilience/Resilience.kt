package io.github.volkanozkan.resilience4jdemo.resilience

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import org.springframework.stereotype.Component

@Component
class Resilience {

    companion object {
        val cbRegistry: CircuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults()
        val rlRegistry: RateLimiterRegistry = RateLimiterRegistry.ofDefaults()
    }

    operator fun <T> invoke(
        name: String,
        rateLimiterConfig: RateLimiterConfiguration? = null,
        block: () -> T
    ): T {
        return resilient(name, rateLimiterConfig, block)
    }

    private fun <T> resilient(
        name: String,
        rateLimiterConfig: RateLimiterConfiguration? = null,
        block: () -> T
    ): T {
        val rateLimiter = createRateLimiter(name, rateLimiterConfig)
        val circuitBreaker = createCircuitBreaker(name)

        val cbDecorated = CircuitBreaker.decorateCheckedSupplier(circuitBreaker, block)
        val decorated = RateLimiter.decorateCheckedSupplier(rateLimiter, cbDecorated)

        return decorated.apply()
    }

    private fun createCircuitBreaker(
        name: String
    ): CircuitBreaker {
        return cbRegistry.circuitBreaker(name) {
            CircuitBreakerConfig.ofDefaults()
        }
    }

    private fun createRateLimiter(
        name: String,
        rateLimiterConfig: RateLimiterConfiguration?
    ): RateLimiter {
        return rlRegistry.rateLimiter(name) {
            rateLimiterConfig?.let { buildRateLimiterConfig(it) } ?: RateLimiterConfig.ofDefaults()
        }
    }

    private fun buildRateLimiterConfig(rlConfig: RateLimiterConfiguration): RateLimiterConfig {
        return RateLimiterConfig.custom()
            .limitForPeriod(rlConfig.limitForPeriod)
            .limitRefreshPeriod(rlConfig.refreshPeriod)
            .timeoutDuration(rlConfig.timeout)
            .build()
    }
}




