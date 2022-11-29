package com.musinsa.board.posts.likereactions.likereaction.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.throwable.shouldHaveMessage

internal class LikedPostTest : StringSpec({
    "likedPostId 는 0보다 크다" {
        //given
        val likedPostId = 1L

        // when then
        shouldNotThrowAny { LikedPost(likedPostId) }
    }

    "likedPostId 는 0보다 작거나 같을 수 없다" {
        setOf(
            -100L,
            -1L,
            0L
        ).forEach {
            shouldThrow<IllegalArgumentException> {
                LikedPost(it)
            }.shouldHaveMessage("likedPostId can not be less than or equal to zero")
        }
    }
})