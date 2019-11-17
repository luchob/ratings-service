package com.java2days.ratings.web

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/admin/healthcheck"], produces = ["text/plain"])
class HealthCheckController {

    @GetMapping
    fun healthCheck(): ResponseEntity<Void> {
        return ok().build()
    }
}
