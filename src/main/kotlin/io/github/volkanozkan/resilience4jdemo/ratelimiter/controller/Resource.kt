package io.github.volkanozkan.resilience4jdemo.ratelimiter.controller

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.ratelimiter.RequestNotPermitted
import io.github.volkanozkan.resilience4jdemo.ratelimiter.service.RateLimiterService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/api")
class Resource(private val rateLimiterService: RateLimiterService) {

    @GetMapping("/rl")
    fun callWithRateLimiter(): ResponseEntity<String> {
        val result = rateLimiterService.callWithRateLimiter()
        return ResponseEntity.ok(result)
    }

    @GetMapping("/cb")
    fun callWithCircuitBreaker(): ResponseEntity<String> {
        val result = rateLimiterService.callWithCircuitBreaker()
        return ResponseEntity.ok(result)
    }

    @ExceptionHandler(RequestNotPermitted::class)
    fun exceptionHandler(exception: RequestNotPermitted): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Reached the rate limit for this api.")
    }

    @ExceptionHandler(CallNotPermittedException::class)
    fun exceptionHandler(exception: CallNotPermittedException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Circuit Breaker is not allowing more calls.")
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun exceptionHandler(exception: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Illegal Argument Exception but circuit breaker is still closed and allows more call.")
    }
}