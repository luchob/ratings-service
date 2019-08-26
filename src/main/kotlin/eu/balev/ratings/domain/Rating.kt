package eu.balev.ratings.domain

import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Size

@Entity
@Table(name = "rating")
data class Rating(

        @Column(nullable = false)
        @Min(0)
        @Max(5)
        val rating: Int,

        @Column(nullable = false)
        @Size(max = 20)
        val comment: String
) {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int = 0
}
