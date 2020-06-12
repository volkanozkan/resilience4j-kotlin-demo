package io.github.volkanozkan.resilience4jdemo

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.volkanozkan.resilience4jdemo.resilience.CircuitBreakerConfiguration
import io.github.volkanozkan.resilience4jdemo.resilience.Resilience
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class CircuitBreakerTest {

    private val resilience = Resilience()

    @Test
    fun `circuit breaker should closed with default CB configs`() {
        val cbName = "test-cb"

        assertThat(CircuitBreaker.State.CLOSED).isEqualTo(Resilience.cbRegistry.circuitBreaker(cbName).state)

        var result = ""
        var i = 0
        repeat(times = 11) {
            try {
                resilience(name = cbName) {
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
                result += "*"
            }
        }

        assertThat("++++*++++*+").isEqualTo(result)
        assertThat(CircuitBreaker.State.CLOSED).isEqualTo(Resilience.cbRegistry.circuitBreaker(cbName).state)
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
                resilience(name = cbName, circuitBreakerConfiguration = cbConfig) {
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
        assertThat(CircuitBreaker.State.OPEN).isEqualTo(Resilience.cbRegistry.circuitBreaker(cbName).state)
    }

}