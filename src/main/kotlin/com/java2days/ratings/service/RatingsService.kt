package com.java2days.ratings.service

import com.java2days.ratings.model.Rating
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class RatingsService(val template: SimpMessagingTemplate) {

    fun sendRating(rating: Rating) {
        this.template.convertAndSend("/ratings", rating)
    }
}
