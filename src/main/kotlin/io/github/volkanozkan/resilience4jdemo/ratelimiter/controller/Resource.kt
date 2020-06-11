package io.github.volkanozkan.resilience4jdemo.ratelimiter.controller

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

    @GetMapping("/")
    fun getFile(): ResponseEntity<String> {
        val result = rateLimiterService.call()
        return ResponseEntity.ok(result)
    }

    @ExceptionHandler(RequestNotPermitted::class)
    fun exceptionHandler(exception: RequestNotPermitted): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Reached the rate limit for this api")
    }
}