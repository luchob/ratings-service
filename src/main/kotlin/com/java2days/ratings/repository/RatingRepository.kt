package com.java2days.ratings.repository

import com.java2days.ratings.model.Rating
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RatingRepository : CrudRepository<Rating, Int> {

  fun findAllByIdAfterOrderById(ratingId: Int): List<Rating>
}
