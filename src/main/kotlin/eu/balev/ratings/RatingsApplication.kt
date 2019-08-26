package eu.balev.ratings

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RatingsApplication

fun main(args: Array<String>) {
	runApplication<RatingsApplication>(*args)
}
