package com.musinsa.board.system

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint

@TestConfiguration
class RestDocsConfig {
    @Bean
    fun restDocsMockMvcConfigurationCustomizer() = RestDocsMockMvcConfigurationCustomizer {
        it.operationPreprocessors()
            .withRequestDefaults(prettyPrint())
            .withResponseDefaults(prettyPrint())
    }
}