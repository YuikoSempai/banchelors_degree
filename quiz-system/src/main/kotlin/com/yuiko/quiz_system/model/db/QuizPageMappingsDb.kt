package com.yuiko.quiz_system.model.db

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("quiz_page_mappings")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
data class QuizPageMappingsDb(
    @Id
    @Column("id")
    val id: Long? = null,
    @Column("main_quiz_id")
    val mainQuizId: Long,
    @Column("page_id")
    val pageId: Long,
    @Column("data")
    val data: String,
    @Column("frontend_data")
    val frontendData: String,
)