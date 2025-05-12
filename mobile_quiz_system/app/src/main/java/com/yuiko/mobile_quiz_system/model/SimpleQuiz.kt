package com.yuiko.mobile_quiz_system.model

data class SimpleQuiz(
    val id: Long,
    val name: String,
    val pageCount: Int,
    val available: Boolean
) {
}