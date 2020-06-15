package io.github.volkanozkan.resilience4jdemo.resilience

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import org.springframework.stereotype.Component

@Component
class ResiliencyHelper {

    companion object {
        val cbRegistry: CircuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults()
        val rlRegistry: RateLimiterRegistry = RateLimiterRegistry.ofDefaults()
    }

    fun <T> runResiliently(
        name: String,
        rateLimiterConfiguration: RateLimiterConfiguration? = null,
        circuitBreakerConfiguration: CircuitBreakerConfiguration? = null,
        block: () -> T
    ): T {
        val rateLimiter = buildRateLimiter(name, rateLimiterConfiguration)
        val circuitBreaker = buildCircuitBreaker(name, circuitBreakerConfiguration)

        val cbDecorated = CircuitBreaker.decorateCheckedSupplier(circuitBreaker, block)
        val decorated = RateLimiter.decorateCheckedSupplier(rateLimiter, cbDecorated)

        return decorated.apply()
    }

    private fun buildCircuitBreaker(
        name: String,
        circuitBreakerConfiguration: CircuitBreakerConfiguration?
    ): CircuitBreaker {
        return cbRegistry.circuitBreaker(name) {
            circuitBreakerConfiguration?.let { buildCircuitBreakerConfig(it) } ?: CircuitBreakerConfig.ofDefaults()
        }.also {
            it.eventPublisher
                .onEvent {
                    //event -> logger.info(...)
                }
        }
    }

    private fun buildRateLimiter(
        name: String,
        rateLimiterConfiguration: RateLimiterConfiguration?
    ): RateLimiter {
        return rlRegistry.rateLimiter(name) {
            rateLimiterConfiguration?.let { buildRateLimiterConfig(it) } ?: RateLimiterConfig.ofDefaults()
        }.also {
            it.eventPublisher
                .onEvent {
                    //event -> logger.info(...)
                }
        }
    }

    private fun buildRateLimiterConfig(rlConfig: RateLimiterConfiguration): RateLimiterConfig {
        return RateLimiterConfig.custom()
            .limitForPeriod(rlConfig.limitForPeriod)
            .limitRefreshPeriod(rlConfig.refreshPeriod)
            .timeoutDuration(rlConfig.timeout)
            .build()
    }

    private fun buildCircuitBreakerConfig(cbConfig: CircuitBreakerConfiguration): CircuitBreakerConfig {
        return CircuitBreakerConfig.custom()
            .failureRateThreshold(cbConfig.failureRateThreshold)
            .slidingWindowSize(cbConfig.slidingWindowSize)
            .permittedNumberOfCallsInHalfOpenState(cbConfig.permittedNumberOfCallsInHalfOpenState)
            .minimumNumberOfCalls(cbConfig.minimumNumberOfCalls)
            .waitDurationInOpenState(cbConfig.waitDurationInOpenState)
            .build()
    }
}




