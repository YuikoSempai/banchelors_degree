package com.yuiko.quiz_system.api

import com.yuiko.quiz_system.model.db.UserDb
import com.yuiko.quiz_system.model.db.UserResultDb
import com.yuiko.quiz_system.repository.UsersRepository
import com.yuiko.quiz_system.repository.UsersResultRepository
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.yuiko.quiz_system.api.UserApiController
import org.yuiko.quiz_system.model.AuthRequest
import org.yuiko.quiz_system.model.RegisterRequest
import org.yuiko.quiz_system.model.UserResultRequest

@RestController
class UsersController(
    val usersRepository: UsersRepository,
    val usersResultRepository: UsersResultRepository
): UserApiController() {

    override fun registerUser(registerRequest: RegisterRequest): ResponseEntity<Unit> {
        val user = usersRepository.findUserDbByUsername(registerRequest.username)
        if (user != null) {
            return ResponseEntity.badRequest().build()
        }
        usersRepository.save(UserDb(username = registerRequest.username, password = registerRequest.password))
        return ResponseEntity.ok().build()
    }

    override fun authUser(authRequest: AuthRequest): ResponseEntity<Long> {
        val user = usersRepository.findUserDbByUsername(authRequest.username)
        if (user == null || authRequest.password != user.password) {
            return ResponseEntity.badRequest().build()
        }
        return ResponseEntity.ok(user.id)
    }

    override fun saveUserResult(userResultRequest: UserResultRequest): ResponseEntity<Unit> {
        usersResultRepository.save(UserResultDb(
            userId = userResultRequest.userId,
            quizId = userResultRequest.quizId,
            correctAnswers = userResultRequest.correctAnswers,
            totalQuestions = userResultRequest.totalQuestions,
        ))
        return ResponseEntity.ok().build()
    }
}