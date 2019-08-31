package eu.balev.ratings.service

import eu.balev.ratings.model.Rating
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class RatingsService(val template: SimpMessagingTemplate) {

    fun sendRating(rating: Rating) {
        this.template.convertAndSend("/ratings", rating)
    }
}
