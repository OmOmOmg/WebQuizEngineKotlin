package engine

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

data class CreateQuiz(
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val text: String,
    @field:Size (min = 2)
    val options: List<String>,
    val answer: List<Int>?
)

//data class NewQuiz(
//    val id: Int,
//    val title: String,
//    val text: String,
//    val options: List<String>,
//    val answer: List<Int>?
//)

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

@RestController
class QuizController(private val repository: QuizRepository) {
//    var idCounter = 0
//    var quizList: MutableList<NewQuiz> = mutableListOf()
//    val quizDB = repository.findAll()
    @PostMapping("/api/quizzes")
    fun postNewQuiz (@RequestBody @Valid newQuiz: CreateQuiz): PublicQuiz {
//        idCounter += 1
        val quiz = NewQuiz (
//            id = idCounter,
            title = newQuiz.title,
            text = newQuiz.text,
            options = newQuiz.options,
            answer = newQuiz.answer
        )
//        quizList.add(quiz)
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
//        if (repository.findById(id).isEmpty) {
//            throw ResponseStatusException(HttpStatus.NOT_FOUND)
//        }
        return repository.findById(id).map { quiz -> PublicQuiz(
            id = quiz.id,
            title = quiz.title,
            text = quiz.text,
            options = quiz.options
        )  }.orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
//        return PublicQuiz(
//            id = quizList[id-1].id,
//            title = quizList[id-1].title,
//            text = quizList[id-1].text,
//            options = quizList[id-1].options
//        )
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
//        val quiz = quizList.find { it.id == id } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val quiz = repository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }
        return if (quiz.answer?.toList() == answer.answer || quiz.answer?.toList() == null && answer.answer!!.isEmpty()) {
            QuizResponse(success = true, feedback = "Congratulations, you're right!")
        } else {
            QuizResponse(success = false, feedback = "Wrong answer! Please, try again. id = $id. quizAnswer = ${quiz.answer} Answer = ${answer.answer}")
        }

    }
}









