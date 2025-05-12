package com.yuiko.quiz_system.repository

import com.yuiko.quiz_system.model.db.QuizRoomMappingDb
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface QuizRoomMappingRepository: CrudRepository<QuizRoomMappingDb, Long> {

    fun findAllByRoomId(roomId: Long): List<QuizRoomMappingDb>
}