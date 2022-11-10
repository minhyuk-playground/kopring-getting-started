package com.musinsa.board.system

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.RequestHeadersSnippet
import org.springframework.restdocs.headers.ResponseHeadersSnippet
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import kotlin.reflect.KClass

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(
    RestDocsConfig::class
)
@Transactional
abstract class AbstractRestControllerTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    protected val defaultRequestHeaderSnippet: RequestHeadersSnippet = HeaderDocumentation.requestHeaders(
        headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON)
    )

    protected val defaultResponseHeaderSnippet: ResponseHeadersSnippet = HeaderDocumentation.responseHeaders(
        headerWithName(HttpHeaders.CONTENT_TYPE).description(MediaType.APPLICATION_JSON)
    )

    protected val pageableParameterDescriptors = arrayOf(
        parameterWithName("page").description("페이지 번호. 0부터 시작. default : 0").optional(),
        parameterWithName("size").description("페이지 데이터 개수. default : 20").optional(),
    )

    protected val pageablePayloadFieldDescriptors = arrayOf(
        fieldWithPath("pageable.*").description("요청한 페이지 정보"),
        fieldWithPath("pageable.sort.*").description("요청한 페이지 정렬 정보"),

        fieldWithPath("totalPages").description("전체 페이지 개수"),
        fieldWithPath("totalElements").description("전체 데이터 개수"),
        fieldWithPath("number").description("현재 페이지 번호"),
        fieldWithPath("size").description("요청한 페이지 데이터 개수"),
        fieldWithPath("numberOfElements").description("현재 페이지에 존재하는 데이터 수"),
        fieldWithPath("first").description("첫번째 페이지인지에 대한 여부"),
        fieldWithPath("last").description("마지막 페이지인지에 대한 여부"),

        fieldWithPath("sort.*").description("페이지의 정렬 정보"),
        fieldWithPath("empty").description("현재 페이지에 데이터가 비어있는지에 대한 여부"),
    )

    @PostConstruct
    fun afterPropertiesSet() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT)
    }

    protected fun objectToJson(obj: Any): String = objectMapper.writeValueAsString(obj)

    private fun <R : Any> jsonToObject(
        json: String,
        returnClass: KClass<R>
    ): R = objectMapper.readValue(json, returnClass.java)
}