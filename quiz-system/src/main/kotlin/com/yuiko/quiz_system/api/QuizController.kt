package com.yuiko.quiz_system.api

import com.yuiko.quiz_system.model.db.QuizDb
import com.yuiko.quiz_system.model.db.QuizPageMappingsDb
import com.yuiko.quiz_system.model.db.UserResultDb
import com.yuiko.quiz_system.model.db.UserRoomMappingDb
import com.yuiko.quiz_system.repository.QuizPageMappingsRepository
import com.yuiko.quiz_system.repository.QuizRepository
import com.yuiko.quiz_system.repository.QuizRoomMappingRepository
import com.yuiko.quiz_system.repository.UserRoomMappingRepository
import com.yuiko.quiz_system.repository.UsersResultRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.yuiko.quiz_system.api.QuizApiController
import org.yuiko.quiz_system.model.PageResponse
import org.yuiko.quiz_system.model.PageSaveRequest
import org.yuiko.quiz_system.model.QuizDto
import org.yuiko.quiz_system.model.QuizResponse
import org.yuiko.quiz_system.model.QuizSaveRequest
import org.yuiko.quiz_system.model.QuizzesResponse
import kotlin.jvm.optionals.getOrNull

@RestController
class QuizController(
    private val quizRepository: QuizRepository,
    private val quizPageMappingsDb: QuizPageMappingsRepository,
    private val usersResultRepository: UsersResultRepository,
    private val quizRoomMappingRepository: QuizRoomMappingRepository,
    private val userRoomMappingRepository: UserRoomMappingRepository,
) : QuizApiController() {

    override fun getQuizById(quizId: Long, pageId: Long?): ResponseEntity<QuizResponse> {
        val page = pageId ?: 1
        val pageCount = quizPageMappingsDb.calculatePagesCount(quizId)
        val mainQuizEntity = quizRepository.findById(quizId).getOrNull()!!
        if (pageCount < 1) {
            return ResponseEntity.ok(
                QuizResponse(
                    id = quizId,
                    name = mainQuizEntity.name,
                    hasNextPage = false,
                    pageCount = pageCount
                )
            )
        }
        val quizPage = quizPageMappingsDb.findQuizPageMappingsDbByMainQuizIdAndPageId(quizId, page)
        return quizPage.let {
            ResponseEntity.ok(
                QuizResponse(
                    id = it.mainQuizId,
                    name = mainQuizEntity.name,
                    divKitData = it.data,
                    frontData = it.frontendData,
                    page = page,
                    hasNextPage = pageCount > page,
                    pageCount = pageCount
                )
            )
        }
    }

    override fun saveQuiz(quizSaveRequest: QuizSaveRequest): ResponseEntity<Long> {
        val quizDb = with(quizSaveRequest) {
            QuizDb(id = quizSaveRequest.id, name = name)
        }

        return ResponseEntity.ok(quizRepository.save(quizDb).id)
    }

    override fun getQuizzes(userId: Long?): ResponseEntity<QuizzesResponse> {
        if (userId == null) {
            return ResponseEntity.ok(
                QuizzesResponse(
                    result = quizRepository.findAll().map { QuizDto(
                        id = it.id!!,
                        name = it.name,
                        pageCount = quizPageMappingsDb.calculatePagesCount(it.id),
                        available = true
                    ) }.filter { it.id == 46L }
                )
            )
        }
        val userQuizzes = getUsersQuizzesId(userId)
        val quizzesList = quizRepository.findAll().filter { userQuizzes.contains(it.id) }
        val userResult: Map<Long, UserResultDb> = usersResultRepository.findAllByUserId(userId)
            .associateBy { it.quizId }
        return ResponseEntity.ok(
            QuizzesResponse(
                result = quizzesList.map {
                    QuizDto(
                        id = it.id!!,
                        name = it.name,
                        pageCount = quizPageMappingsDb.calculatePagesCount(it.id),
                        available = !userResult.containsKey(it.id)
                    )
                }.sortedByDescending { it.available },
            )
        )
    }

    override fun getPageById(quizId: Long, pageId: Long): ResponseEntity<PageResponse> {
        val quizPage = quizPageMappingsDb.findQuizPageMappingsDbByMainQuizIdAndPageId(quizId, pageId)
        return ResponseEntity.ok(
            with(quizPage) {
                PageResponse(
                    quizId = this.mainQuizId,
                    pageId = this.pageId,
                    divKitData = this.data,
                    rawData = this.frontendData
                )
            }
        )
    }

    override fun getPages(): ResponseEntity<Unit> {
        return super.getPages()
    }

    override fun savePage(quizId: Long, pageSaveRequest: PageSaveRequest): ResponseEntity<Unit> {
        val currentPageCount = quizPageMappingsDb.calculatePagesCount(quizId)
        if (pageSaveRequest.id == null) {
            quizPageMappingsDb.save(
                QuizPageMappingsDb(
                    mainQuizId = quizId,
                    pageId = currentPageCount + 1,
                    data = pageSaveRequest.divKitData,
                    frontendData = pageSaveRequest.rawData
                )
            )
        } else {
            quizPageMappingsDb.saveQuizByQuizIdAndPageId(
                quizId = quizId,
                pageId = pageSaveRequest.id,
                frontData = pageSaveRequest.rawData,
                divKitData = pageSaveRequest.divKitData,
            )
        }
        return ResponseEntity.ok().build()
    }

    private fun getUsersQuizzesId(userId: Long): List<Long> {
        val userRooms = userRoomMappingRepository.findAllByUserId(userId).map { it.roomId }
        val userQuizzes = userRooms.flatMap { quizRoomMappingRepository.findAllByRoomId(it) }
            .map { it.quizId }.distinct()
        return userQuizzes
    }
}