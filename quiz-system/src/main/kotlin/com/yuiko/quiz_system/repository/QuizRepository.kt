package com.yuiko.quiz_system.repository

import com.yuiko.quiz_system.model.db.QuizDb
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface QuizRepository: CrudRepository<QuizDb, Long> {
}