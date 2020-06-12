package io.github.volkanozkan.resilience4jdemo

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.volkanozkan.resilience4jdemo.resilience.CircuitBreakerConfiguration
import io.github.volkanozkan.resilience4jdemo.resilience.Resilience
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class CircuitBreakerTest {

    private val resilience = Resilience()

    @Test
    fun `circuit breaker should closed with default CB configs`() {
        val cbName = "test-cb"

        assertEquals(CircuitBreaker.State.CLOSED, Resilience.cbRegistry.circuitBreaker(cbName).state)

        var output = ""
        var i = 0
        repeat(times = 11) {
            try {
                resilience(name = cbName) {
                    i++
                    if (i == 5 || i == 10) {
                        throw Exception("CB")
                    }
                    output += "."
                    i
                }
            } catch (e: CallNotPermittedException) {
                output += "-"
            } catch (e: Exception) {
                output += "*"
            }
        }

        assertEquals("....*....*.", output)
        assertEquals(CircuitBreaker.State.CLOSED, Resilience.cbRegistry.circuitBreaker(cbName).state)
    }

    @Test
    fun `circuit breaker should be open with overridden configs`() {
        val cbConfig = CircuitBreakerConfiguration(
            failureRateThreshold = 60F,
            minimumNumberOfCalls = 5
        )

        val cbName = "test-cb2"

        var output = ""
        var i = 0
        repeat(times = 15) {
            try {
                resilience(name = cbName, circuitBreakerConfiguration = cbConfig) {
                    i++
                    if (i >= 2) {
                        throw Exception("CB")
                    }
                    output += "+"
                    i
                }
            } catch (e: CallNotPermittedException) {
                output += "-"
            } catch (e: Exception) {
                output += "*"
            }
        }

        assertEquals("+**------------", output)
        assertEquals(CircuitBreaker.State.OPEN, Resilience.cbRegistry.circuitBreaker(cbName).state)
    }

}