package eu.balev.ratings.web

import eu.balev.ratings.model.Rating
import eu.balev.ratings.repository.RatingRepository
import eu.balev.ratings.service.RatingsService
import org.slf4j.LoggerFactory
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@CrossOrigin(origins = ["*"])
@RestController
class RatingsController(val ratingRepository: RatingRepository,
                        val ratingService: RatingsService) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RatingsController::class.java)
    }

    @PostMapping("/ratings")
    fun createRating(@Valid @RequestBody rating: Rating) {

        LOGGER.debug("Received a rating, trying to save...");

        this.ratingRepository.save(rating);
        this.ratingService.sendRating(rating)
    }

}
