package com.yuiko.quiz_system.repository

import com.yuiko.quiz_system.model.db.UserRoomMappingDb
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRoomMappingRepository : CrudRepository<UserRoomMappingDb, Long> {

    fun findAllByRoomId(roomId: Long): List<UserRoomMappingDb>

    fun findAllByUserId(userId: Long): List<UserRoomMappingDb>
}