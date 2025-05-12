package com.yuiko.quiz_system.repository

import com.yuiko.quiz_system.model.db.UserDb
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UsersRepository : CrudRepository<UserDb, Long> {
    fun findUserDbByUsername(username: String): UserDb?

    fun findUserById(id: Long): UserDb?
}