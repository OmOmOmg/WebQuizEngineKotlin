package engine

import com.sun.tools.javac.jvm.ByteCodes.ret
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
class QuizController(
    private val repository: QuizRepository,
    private val AppUserRepository: AppUserRepository,
    private val passwordEncoder: PasswordEncoder) {
    @PostMapping("/api/quizzes")
    fun postNewQuiz (
        @RequestBody @Valid newQuiz: CreateQuiz,
        authentication: Authentication): PublicQuiz {

        val quiz = NewQuiz (
            title = newQuiz.title,
            text = newQuiz.text,
            options = newQuiz.options,
            answer = newQuiz.answer,
            createdBy = authentication.name
        )

        repository.save(quiz)
        return PublicQuiz(
            id = quiz.id,
            title = quiz.title,
            text = quiz.text,
            options = quiz.options
        )
    }
    @GetMapping("/api/quizzes/{id}")
    fun getQuiz(@PathVariable id: Int): PublicQuiz {
        return repository.findById(id).map { quiz -> PublicQuiz(
            id = quiz.id,
            title = quiz.title,
            text = quiz.text,
            options = quiz.options
        )  }.orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

    }

    @GetMapping("/api/quizzes")
    fun getAllQuizzes(): List<PublicQuiz> {
        return repository.findAll().map {
            PublicQuiz(
                id = it.id,
                title = it.title,
                text = it.text,
                options = it.options
            )
        }
    }

    @PostMapping("/api/quizzes/{id}/solve")
    fun solveQuiz(@PathVariable id: Int, @RequestBody answer: Answer): QuizResponse {
        val quiz = repository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        return if (quiz.answer?.toList() == answer.answer || quiz.answer?.toList() == null && answer.answer!!.isEmpty()) {
            QuizResponse(success = true, feedback = "Congratulations, you're right!")
        } else {
            QuizResponse(success = false, feedback = "Wrong answer! Please, try again. id = $id. quizAnswer = ${quiz.answer} Answer = ${answer.answer}")
        }

    }

    @PostMapping("/api/register")
    fun register(@Valid @RequestBody user: AppUser) {
        if (AppUserRepository.findAppUserByEmail(user.email) != null) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists")
        }
        if ((user.password?.length ?: 0) < 5) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Password should be at least 5 characters long")
        }
        user.password = passwordEncoder.encode(user.password)
        AppUserRepository.save(user)
    }

    @DeleteMapping("/api/quizzes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteQuiz(@PathVariable id: Int) {
        if (!repository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }
        if (repository.findById(id).get().createdBy != SecurityContextHolder.getContext().authentication.name) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN)
        }
        repository.deleteById(id)

    }

}