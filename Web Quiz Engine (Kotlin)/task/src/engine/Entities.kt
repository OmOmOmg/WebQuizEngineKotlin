package engine

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.time.Instant


data class CreateQuiz(
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val text: String,
    @field:Size(min = 2)
    val options: List<String>,
    val answer: List<Int>?
)

data class PublicQuiz(
    val id: Int,
    val title: String,
    val text: String,
    val options: List<String>,
)

data class Answer(
    val answer: List<Int>?,
)

data class QuizResponse(
    val success: Boolean,
    val feedback: String
)

@Entity
data class NewQuiz(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val title: String,
    val text: String,
    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    val options: List<String>,
    @ElementCollection(fetch = FetchType.EAGER)
    val answer: List<Int>?,
    val createdBy: String
)

@Entity
data class AppUser(
    @Id
    @field:Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    val email: String? = null,
    @field:Size(min = 5)
    var password: String? = null,
    @ElementCollection(fetch = FetchType.EAGER)
    val quizzesSolved: MutableMap<Int, Instant>
)

data class CompletedQuiz(
    val id: Int,
    val completedAt: Instant
)