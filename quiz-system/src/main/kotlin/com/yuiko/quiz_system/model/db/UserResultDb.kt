package com.yuiko.quiz_system.model.db

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("users_result")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
data class UserResultDb(
    @Id
    @Column("id")
    val id: Long? = null,

    @Column("user_id")
    val userId: Long,

    @Column("quiz_id")
    val quizId: Long,

    @Column("correct_answers")
    val correctAnswers: Long,

    @Column("total_questions")
    val totalQuestions: Long,
)
