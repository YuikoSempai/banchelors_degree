package com.yuiko.quiz_system.repository

import com.yuiko.quiz_system.model.db.UserResultDb
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersResultRepository: CrudRepository<UserResultDb, Long> {

    fun findAllByUserId(userId: Long): List<UserResultDb>

    fun findAllByUserIdAndQuizIdIn(userId: Long, roomId: List<Long>): List<UserResultDb>
}