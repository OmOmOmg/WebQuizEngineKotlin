package engine

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import java.time.Instant

interface QuizRepository: CrudRepository<NewQuiz, Int>, PagingAndSortingRepository<NewQuiz, Int> {
}

interface AppUserRepository : CrudRepository<AppUser, String>, PagingAndSortingRepository<AppUser, String> {
    fun findAppUserByEmail(email: String?): AppUser?
}

//interface CompletedQuizRepository : CrudRepository<CompletedQuiz, Instant>, PagingAndSortingRepository<CompletedQuiz, Instant>
