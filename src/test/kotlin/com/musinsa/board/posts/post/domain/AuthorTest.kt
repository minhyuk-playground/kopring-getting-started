package com.musinsa.board.posts.post.domain

import org.assertj.core.api.Assertions.assertThatNoException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class AuthorTest {
    @ParameterizedTest
    @ValueSource(longs = [1, 2, 3, 4, 5, 100])
    internal fun `authorId 는 0보다 커야 한다`(authorId: Long) {
        // given when then
        assertThatNoException().isThrownBy { Author(authorId) }
    }

    @ParameterizedTest
    @ValueSource(longs = [0, -1, -2, -5, -10, -100])
    internal fun `authorId 가 0보다 작거나 같다면 예외가 발생해야한다`(authorId: Long) {
        // given when then
        assertThatThrownBy { Author(authorId) }
            .isExactlyInstanceOf(IllegalArgumentException::class.java)
    }
}