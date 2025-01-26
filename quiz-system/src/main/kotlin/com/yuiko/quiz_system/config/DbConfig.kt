package com.yuiko.quiz_system.config

import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import javax.sql.DataSource

@Configuration
@EnableJdbcRepositories("com.yuiko.quiz_system.repository")
class DbConfig {

    @Bean
    fun dataSource(
        @Value("\${spring.datasource.url:}")
        dataSourceUrl: String,
        @Value("\${spring.datasource.username:}")
        username: String,
        @Value("\${spring.datasource.password:}")
        password: String
    ): DataSource {
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName("org.postgresql.Driver")
        dataSourceBuilder.url(dataSourceUrl)
        dataSourceBuilder.username(username)
        dataSourceBuilder.password(password)
        return dataSourceBuilder.build()
    }

    @Bean
    fun liquibase(
        dataSource: DataSource,
    ): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.dataSource = dataSource
        liquibase.changeLog = "classpath:liquibase/db-changelog.xml"
        return liquibase
    }
}