package com.java2days.ratings.model

import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Size

@Entity
@Table(name = "rating")
data class Rating(

        @get: [Column(nullable = false) Min(0) Max(5)]
        val rating: Int,

        @get: [Column(nullable = false) Size(max = 255)]
        val comment: String
) {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0
}
