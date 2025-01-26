package com.yuiko.quiz_system.config

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.yuiko.quiz_system.model.db.QuizDb
import org.postgresql.util.PGobject
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration

// @Configuration
class ConversionConfig(
    private val objectMapper: ObjectMapper,
): AbstractJdbcConfiguration() {

    // @Bean
    // override fun jdbcCustomConversions() = JdbcCustomConversions(
    //     listOf(
    //         QuizWritingConverter(objectMapper),
    //         QuizReadingConverter(objectMapper),
    //     )
    // )
}

// @WritingConverter
// class QuizWritingConverter(
//     private val objectMapper: ObjectMapper
// ): Converter<QuizDb, PGobject> {
//     override fun convert(source: QuizDb) = PGobject().apply {
//         type = "json"
//         value = objectMapper.writeValueAsString(source)
//     }
// }
//
// @ReadingConverter
// class QuizReadingConverter(
//     private val objectMapper: ObjectMapper,
// ) : Converter<PGobject, QuizDb> {
//
//     private val payloadTypeReference = object : TypeReference<QuizDb>() {}
//
//     override fun convert(pgObject: PGobject): QuizDb {
//         val source = pgObject.value
//         return objectMapper.readValue(source, payloadTypeReference)
//     }
// }