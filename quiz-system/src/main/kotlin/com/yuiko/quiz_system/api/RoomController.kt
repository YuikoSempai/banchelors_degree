package com.yuiko.quiz_system.api

import com.yuiko.quiz_system.model.db.QuizDb
import com.yuiko.quiz_system.model.db.UserDb
import com.yuiko.quiz_system.model.db.UserResultDb
import com.yuiko.quiz_system.repository.QuizRepository
import com.yuiko.quiz_system.repository.QuizRoomMappingRepository
import com.yuiko.quiz_system.repository.RoomRepository
import com.yuiko.quiz_system.repository.UserRoomMappingRepository
import com.yuiko.quiz_system.repository.UsersRepository
import com.yuiko.quiz_system.repository.UsersResultRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.yuiko.quiz_system.api.RoomApiController
import org.yuiko.quiz_system.model.RoomDto
import org.yuiko.quiz_system.model.RoomResultDto
import org.yuiko.quiz_system.model.RoomResultResponse
import org.yuiko.quiz_system.model.RoomsResponse

@RestController
class RoomController(
    private val roomRepository: RoomRepository,
    private val quizRoomMappingRepository: QuizRoomMappingRepository,
    private val userRoomMappingRepository: UserRoomMappingRepository,
    private val usersRepository: UsersRepository,
    private val quizRepository: QuizRepository,
    private val resultRepository: UsersResultRepository
) : RoomApiController() {

    override fun getRooms(): ResponseEntity<RoomsResponse> {
        val rooms = roomRepository.findAll()
        // val users =
        return ResponseEntity.ok(
            RoomsResponse(
                result = rooms.map {
                    RoomDto(
                        id = it.id!!,
                        name = it.name,
                        users = findUserMappings(it.id).map(UserDb::username),
                        quizNames = findQuizMapping(it.id).map(QuizDb::name),
                    )
                }.filter { it.id == 5L }
            )
        )
    }

    override fun getRoomResult(roomId: Long): ResponseEntity<RoomResultResponse> {
        val userMappings = userRoomMappingRepository.findAllByRoomId(roomId)
            .mapNotNull { usersRepository.findUserById(it.userId) }
        val quizzes = quizRoomMappingRepository.findAllByRoomId(roomId)
        val quizzesCount = quizzes.size
        val results: MutableMap<UserDb, MutableList<Double>> = userMappings
            .associateWith {
                resultRepository.findAllByUserIdAndQuizIdIn(it.id!!, quizzes.map { q -> q.quizId })
                    .map { res -> res.correctAnswers / (res.totalQuestions / 100.0) }.toMutableList()
            }
            .toMutableMap()
        for (entry in results) {
            if (entry.value.size < quizzesCount) {
                val currentValue = entry.value.size
                for (i in currentValue until quizzesCount) {
                    entry.value.add(0.0)
                }
            }
        }
        return ResponseEntity.ok(
            RoomResultResponse(
                results = results.map {
                    RoomResultDto(
                        userName = it.key.username,
                        quizResults = it.value.map { v -> String.format("%.0f", v).toDouble() }
                    )
                }.sortedByDescending { it.quizResults.sum() }
            )
        )
    }

    private fun findUserMappings(roomId: Long): List<UserDb> {
        val mappings = userRoomMappingRepository.findAllByRoomId(roomId)
        return mappings.mapNotNull {
            usersRepository.findUserById(it.userId)
        }
    }

    private fun findQuizMapping(roomId: Long): List<QuizDb> {
        return quizRoomMappingRepository.findAllByRoomId(roomId)
            .mapNotNull {
                quizRepository.findQuizById(it.quizId)
            }
    }
}