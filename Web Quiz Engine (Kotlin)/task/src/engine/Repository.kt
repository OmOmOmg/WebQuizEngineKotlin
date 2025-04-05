package engine

import org.springframework.data.repository.CrudRepository

interface QuizRepository: CrudRepository <NewQuiz, Int> {
}

interface AppUserRepository : CrudRepository<AppUser, String> {
    fun findAppUserByEmail(email: String?): AppUser?
}