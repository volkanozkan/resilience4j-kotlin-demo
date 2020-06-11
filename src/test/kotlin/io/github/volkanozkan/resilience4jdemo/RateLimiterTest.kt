package io.github.volkanozkan.resilience4jdemo

import io.github.resilience4j.ratelimiter.RequestNotPermitted
import io.github.volkanozkan.resilience4jdemo.resilience.RateLimiterConfiguration
import io.github.volkanozkan.resilience4jdemo.resilience.Resilience
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RateLimiterTest {

    private val resilience = Resilience()

    @Test
    fun `rate limiter test`() {
        val rlConfig = RateLimiterConfiguration.defaultRLConfiguration

        var result = ""

        repeat(times = 5) {
            result += try {
                resilience(name = "test-rl", rateLimiterConfig = rlConfig) {
                    "OK"
                }
                "+"
            } catch (e: RequestNotPermitted) {
                "-"
            } catch (e: Exception) {
                "?"
            }
        }

        assertThat("++---").isEqualTo(result)
    }

}
