package com.musinsa.board.posts.post.interfaces.rest

import com.musinsa.board.posts.post.domain.*
import com.musinsa.board.posts.post.interfaces.rest.payload.ContentPayload
import com.musinsa.board.posts.post.interfaces.rest.payload.PostPayload
import com.musinsa.board.system.AbstractRestControllerTest
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.*
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

internal class PostRestControllerTest(
    @Autowired private val postRepository: PostRepository
) : AbstractRestControllerTest() {
    private val postIdPathParametersSnippet = pathParameters(
        parameterWithName("id").description("게시물 ID")
    )

    private val defaultPublishedPost = Post(
        author = Author(1L),
        _content = Content(title = "title", body = "body"),
        _status = PostStatus.PUBLISHED
    )

    @Test
    internal fun `게시물 목록 조회`() {
        // given
        postRepository.save(defaultPublishedPost)

        // when then
        mockMvc.get("/posts") {
            contentType = MediaType.APPLICATION_JSON
            param("page", "0")
            param("size", "100")

        }.andExpect {
            status { isOk() }
        }.andDo {
            print()
            handle(
                document(
                    "get-all-posts",
                    defaultRequestHeaderSnippet,
                    requestParameters(*pageableParameterDescriptors),
                    defaultResponseHeaderSnippet,
                    responseFields(
                        fieldWithPath("content[].id").description("게시물 ID"),
                        fieldWithPath("content[].author.id").description("게시물 작성자 ID"),
                        fieldWithPath("content[].content.title").description("게시물 제목"),
                        fieldWithPath("content[].content.body").description("게시물 본문 내용"),
                        fieldWithPath("content[].status").description("게시물의 상태.[PUBLISHED, DELETED]"),
                        *pageablePayloadFieldDescriptors
                    )
                )
            )
        }
    }

    @Test
    internal fun `게시물 단건 조회`() {
        // given
        val post = postRepository.save(defaultPublishedPost)

        // when then
        mockMvc
            .perform(
                get("/posts/{id}", post.id)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "get-post",
                    defaultRequestHeaderSnippet,
                    postIdPathParametersSnippet,
                    defaultRequestHeaderSnippet,
                    responseFields(
                        fieldWithPath("id").description("게시물 ID"),
                        fieldWithPath("author.id").description("게시물 작성자 ID"),
                        fieldWithPath("content.title").description("게시물 제목"),
                        fieldWithPath("content.body").optional().description("게시물 본문 내용"),
                        fieldWithPath("status").description("게시물의 상태.[PUBLISHED, DELETED]"),
                    )
                )
            )
    }

    @Test
    internal fun `게시물 발행`() {
        // given
        val postPayload = PostPayload(content = ContentPayload(title = "title", body = "body"))

        // when then
        mockMvc.post("/posts") {
            contentType = MediaType.APPLICATION_JSON
            content = objectToJson(postPayload)
        }.andExpect {
            status { isCreated() }
        }.andDo {
            print()
            handle(
                document(
                    "publish-post",
                    defaultRequestHeaderSnippet,
                    requestFields(
                        fieldWithPath("content.title").description("게시물 제목"),
                        fieldWithPath("content.body").optional().description("게시물 본문 내용"),
                    ),
                    responseHeaders(
                        headerWithName(HttpHeaders.LOCATION).description("생성된 게시물 단건 조회 링크")
                    )
                )
            )
        }
    }

    @Test
    internal fun `게시물 수정`() {
        // given
        val post = postRepository.save(defaultPublishedPost)
        val newContent = Content(title = "newTitle", body = "newBody")

        // when then
        mockMvc
            .perform(
                patch("/posts/{id}/content", post.id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectToJson(newContent))
            )
            .andExpect(status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "modify-post",
                    defaultRequestHeaderSnippet,
                    postIdPathParametersSnippet,
                    requestFields(
                        fieldWithPath("title").description("게시물 제목"),
                        fieldWithPath("body").optional().description("게시물 본문 내용"),
                    )
                )
            )
    }

    @Test
    internal fun `게시물 삭제`() {
        // given
        val post = postRepository.save(defaultPublishedPost)

        // when then
        mockMvc
            .perform(
                delete("/posts/{id}", post.id)
            )
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "delete-post",
                    postIdPathParametersSnippet
                )
            )
    }
}