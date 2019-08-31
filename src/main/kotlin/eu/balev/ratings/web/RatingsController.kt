package eu.balev.ratings.web

import eu.balev.ratings.model.Rating
import eu.balev.ratings.repository.RatingRepository
import eu.balev.ratings.service.RatingsService
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class RatingsController(val ratingRepository: RatingRepository,
                        val ratingService: RatingsService) {

    @MessageMapping("/ratings")
    @SendTo("/topic/ratings")
    @Throws(Exception::class)
    fun ratings(): Rating {
        Thread.sleep(1000) // simulated delay
        return Rating(5, "So true!")
    }

    @PostMapping("/ratings")
    fun createRating(@Valid @RequestBody rating: Rating) {
        this.ratingRepository.save(rating);
        this.ratingService.sendRating(rating)
    }

}
