package io.github.volkanozkan.resilience4jdemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Resilience4jDemoApplication

fun main(args: Array<String>) {
	runApplication<Resilience4jDemoApplication>(*args)
}
