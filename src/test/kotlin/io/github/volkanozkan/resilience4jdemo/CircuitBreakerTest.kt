package io.github.volkanozkan.resilience4jdemo

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.volkanozkan.resilience4jdemo.resilience.CircuitBreakerConfiguration
import io.github.volkanozkan.resilience4jdemo.resilience.ResiliencyHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class CircuitBreakerTest {

    private val resiliencyHelper = ResiliencyHelper()

    @Test
    fun `circuit breaker should closed with default CB configs`() {
        val cbName = "test-cb"

        assertThat(CircuitBreaker.State.CLOSED).isEqualTo(ResiliencyHelper.cbRegistry.circuitBreaker(cbName).state)

        var result = ""
        var i = 0
        repeat(times = 11) {
            try {
                resiliencyHelper.runResiliently(name = cbName) {
                    i++
                    if (i == 5 || i == 10) {
                        throw Exception("CB")
                    }
                    result += "+"
                    i
                }
            } catch (e: CallNotPermittedException) {
                result += "-"
            } catch (e: Exception) {
                result += "?"
            }
        }

        assertThat("++++?++++?+").isEqualTo(result)
        assertThat(CircuitBreaker.State.CLOSED).isEqualTo(ResiliencyHelper.cbRegistry.circuitBreaker(cbName).state)
    }

    @Test
    fun `circuit breaker should be open with overridden configs`() {
        val cbConfig = CircuitBreakerConfiguration(
            failureRateThreshold = 60F,
            minimumNumberOfCalls = 5
        )

        val cbName = "test-cb2"

        var result = ""
        var i = 0
        repeat(times = 15) {
            try {
                resiliencyHelper.runResiliently(name = cbName, circuitBreakerConfiguration = cbConfig) {
                    i++
                    if (i >= 2) {
                        throw Exception("CB")
                    }
                    result += "+"
                    i
                }
            } catch (e: CallNotPermittedException) {
                result += "-"
            } catch (e: Exception) {
                result += "?"
            }
        }

        assertThat("+??------------").isEqualTo(result)
        assertThat(CircuitBreaker.State.OPEN).isEqualTo(ResiliencyHelper.cbRegistry.circuitBreaker(cbName).state)
    }

}