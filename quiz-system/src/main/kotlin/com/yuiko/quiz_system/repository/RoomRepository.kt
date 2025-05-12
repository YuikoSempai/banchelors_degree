package com.yuiko.quiz_system.repository

import com.yuiko.quiz_system.model.db.RoomDb
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoomRepository : CrudRepository<RoomDb, Long> {
}
