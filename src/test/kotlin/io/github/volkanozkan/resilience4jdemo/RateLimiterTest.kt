package io.github.volkanozkan.resilience4jdemo

import io.github.resilience4j.ratelimiter.RequestNotPermitted
import io.github.volkanozkan.resilience4jdemo.resilience.RateLimiterConfiguration
import io.github.volkanozkan.resilience4jdemo.resilience.ResiliencyHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Duration

class RateLimiterTest {

    private val resiliencyHelper = ResiliencyHelper()

    @Test
    fun `rate limiter test`() {
        val rlConfig = RateLimiterConfiguration()

        var result = ""

        repeat(times = 5) {
            result += try {
                resiliencyHelper.runResiliently(name = "test-rl", rateLimiterConfiguration = rlConfig) {
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

    @Test
    fun `rate limiter should allow with 1 second timeout`() {
        val rlConfig = RateLimiterConfiguration(
            limitForPeriod = 1,
            timeout = Duration.ofSeconds(1),
            refreshPeriod = Duration.ofSeconds(1)
        )

        var result = ""

        repeat(times = 8) {
            result += try {
                resiliencyHelper.runResiliently(name = "test-rl-2", rateLimiterConfiguration = rlConfig) {
                    "OK"
                }
                "+"
            } catch (e: RequestNotPermitted) {
                "-"
            } catch (e: Exception) {
                "?"
            }
        }

        assertThat("++++++++").isEqualTo(result)
    }

}
