package com.yuiko.mobile_quiz_system.model

data class Quiz(
    val id: Long,
    val name: String,
    val divKitData: String,
    val frontData: String,
    val hasNextPage: Boolean,
    val page: Long,
    val pageCount: Long
)
