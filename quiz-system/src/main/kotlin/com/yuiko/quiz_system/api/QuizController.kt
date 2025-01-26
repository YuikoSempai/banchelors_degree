package com.yuiko.quiz_system.api

import com.yuiko.quiz_system.model.db.QuizDb
import com.yuiko.quiz_system.repository.QuizRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.yuiko.quiz_system.api.QuizApiController
import org.yuiko.quiz_system.model.QuizResponse
import org.yuiko.quiz_system.model.QuizSaveRequest
import kotlin.jvm.optionals.getOrNull

@RestController
class QuizController(
    private val quizRepository: QuizRepository
) : QuizApiController() {

    override fun getQuizById(quizId: Long): ResponseEntity<QuizResponse> {
        val quiz = quizRepository.findById(quizId).getOrNull()
        return quiz?.let {
            ResponseEntity.ok(QuizResponse(id = it.id!!, name = it.name, data = it.data))
        } ?: ResponseEntity.badRequest().build()
    }

    override fun saveQuiz(quizSaveRequest: QuizSaveRequest): ResponseEntity<Long> {
        val quizDb = with(quizSaveRequest) {
            QuizDb(name = name, data = data)
        }
        return ResponseEntity.ok(quizRepository.save(quizDb).id)
    }
}