package com.java2days.ratings.web

import com.java2days.ratings.model.Rating
import com.java2days.ratings.repository.RatingRepository
import com.java2days.ratings.service.RatingsService
import org.slf4j.LoggerFactory
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

  @GetMapping("/ratings/{lastRatingId}")
  fun getRatings(@PathVariable("lastRatingId") lastRatingId: Int): Iterable<Rating> {
    LOGGER.debug("Get all ratings after rating: {}.", lastRatingId);
    return this.ratingRepository.findAllByIdAfterOrderById(lastRatingId)
  }

}
