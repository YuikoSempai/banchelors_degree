package com.yuiko.quiz_system.model.db

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("quiz_room_mapping")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
data class QuizRoomMappingDb(
    @Id
    @Column("id")
    val id: Int,
    @Column("quiz_id")
    val quizId: Long,
    @Column("room_id")
    val roomId: Long,
)
