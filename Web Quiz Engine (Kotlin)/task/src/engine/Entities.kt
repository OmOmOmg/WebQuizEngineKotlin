package engine

import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

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
//    @Fetch(value = FetchMode.SUBSELECT)
    val answer: List<Int>?
)