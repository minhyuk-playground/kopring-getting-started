package com.musinsa.board.posts.post.domain

import org.assertj.core.api.Assertions.assertThatNoException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource

internal class ContentTest {

    @ParameterizedTest
    @ValueSource(strings = ["한", "한글", "한글자", "한글자 이상"])
    internal fun `게시물은 반드시 1글자 이상의 제목을 가지고 있어야 한다`(
        title: String
    ) {
        // given when then
        assertThatNoException().isThrownBy {
            Content(title)
        }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "     ", "          "])
    internal fun `게시물의 제목이 빈문자열이라면 게시물을 등록할 수 없다`(
        title: String
    ) {
        // given when then
        assertThatThrownBy { Content(title) }
            .isExactlyInstanceOf(IllegalArgumentException::class.java)
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = [" ", "  ", "     ", "          "])
    internal fun `게시물의 본문은 비어있을 수 있다`(
        body: String?
    ) {
        // given when then
        assertThatNoException().isThrownBy { Content(title = "title", body = body) }
    }
}