package eu.balev.ratings.web

import eu.balev.ratings.domain.Rating
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping

@Controller
class RatingsController {

    @MessageMapping("/ratings")
    @SendTo("/topic/ratings")
    @Throws(Exception::class)
    fun ratings(): Rating {
        Thread.sleep(1000) // simulated delay
        return Rating(5, "So true!")
    }

    @PostMapping
    fun createRating(rating: Rating) {
        println(rating)
    }
}
