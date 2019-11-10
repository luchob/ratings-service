package eu.balev.ratings.repository

import eu.balev.ratings.model.Rating
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RatingRepository : CrudRepository<Rating, Int> {

  fun findAllByIdAfterOrderById(ratingId: Int): List<Rating>
}
