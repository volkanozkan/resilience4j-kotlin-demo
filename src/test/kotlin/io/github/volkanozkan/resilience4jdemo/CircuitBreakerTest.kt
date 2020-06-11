package io.github.volkanozkan.resilience4jdemo

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.volkanozkan.resilience4jdemo.resilience.Resilience
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class CircuitBreakerTest {

    private val resilience = Resilience()

    @Test
    fun `circuit breaker test`() {
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
            } catch (e: Exception) {
                output += "x"
            }
        }

        assertEquals("....x....x.", output)
        assertEquals(CircuitBreaker.State.CLOSED, Resilience.cbRegistry.circuitBreaker(cbName).state)
    }

}