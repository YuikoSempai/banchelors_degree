package com.yuiko.quiz_system.repository

import com.yuiko.quiz_system.model.db.QuizPageMappingsDb
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
    interface QuizPageMappingsRepository: CrudRepository<QuizPageMappingsDb, Long> {
    fun findQuizPageMappingsDbByMainQuizIdAndPageId(quizId: Long, pageId: Long): QuizPageMappingsDb

    @Query("""
        select count(*) from quiz_page_mappings
        where main_quiz_id = :quizId
    """)
    fun calculatePagesCount(quizId: Long): Long

    @Query("""
        update quiz_page_mappings 
        set data = :divKitData, frontend_data = :frontData
        where main_quiz_id = :quizId and page_id = :pageId
    """)
    @Modifying
    fun saveQuizByQuizIdAndPageId(quizId: Long, pageId: Long, divKitData: String, frontData: String)
}