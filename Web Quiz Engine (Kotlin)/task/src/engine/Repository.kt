package engine

import org.springframework.data.repository.CrudRepository

interface QuizRepository: CrudRepository <NewQuiz, Int> {
}