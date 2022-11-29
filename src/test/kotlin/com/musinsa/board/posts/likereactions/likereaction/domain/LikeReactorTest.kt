package com.musinsa.board.posts.likereactions.likereaction.domain

import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class LikeReactorTest {
    @Test
    internal fun `reactorId 는 0보다 크다`() {
        // given
        val reactorId = 1L

        // when then
        assertThatCode {
            LikeReactor(reactorId)
        }.doesNotThrowAnyException()
    }

    @ParameterizedTest
    @ValueSource(longs = [-100L, -1L, 0L])
    internal fun `reactorId 는 0보다 작거나 같을 수 없다`(reactor: Long) {
        // given when then
        assertThatThrownBy { LikeReactor(reactor) }
            .isExactlyInstanceOf(IllegalArgumentException::class.java)
    }
}