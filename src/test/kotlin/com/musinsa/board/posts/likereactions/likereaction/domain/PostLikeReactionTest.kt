package com.musinsa.board.posts.likereactions.likereaction.domain

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

internal class PostLikeReactionTest : StringSpec({
    val doNothingValidator: PostLikeReactionValidator = object : PostLikeReactionValidator {
        override fun validate(postLikeReaction: PostLikeReaction) {
            // do nothing
        }
    }

    val exceptionValidator: PostLikeReactionValidator = object : PostLikeReactionValidator {
        override fun validate(postLikeReaction: PostLikeReaction) {
            throw RuntimeException()
        }
    }

    fun createPostLikeReaction(
        likeReactor: LikeReactor = LikeReactor(reactorId = 1L),
        likedPost: LikedPost = LikedPost(likedPostId = 1L),
        status: PostLikeReactionStatus = PostLikeReactionStatus.LIKED
    ): PostLikeReaction = PostLikeReaction(likeReactor, likedPost, status)

    "게시물 좋아요 반응자가 게시물을 좋아하면 게시물 좋아요 반응 상태는 LIKED 이어야 한다" {
        // given
        val postLikeReaction = createPostLikeReaction(status = PostLikeReactionStatus.LIKED)

        // when
        postLikeReaction.create(doNothingValidator)

        // then
        postLikeReaction.status shouldBe PostLikeReactionStatus.LIKED
    }

    "게시물 좋아요 반응이 생성되면 PostLikeReactionCreatedEvent 가 발행되어야 한다" {
        // given
        val postLikeReaction = createPostLikeReaction(status = PostLikeReactionStatus.LIKED)

        // when
        postLikeReaction.create(doNothingValidator)

        // then
        postLikeReaction.publishedEvents.shouldContainExactly(PostLikeReactionCreatedEvent(postLikeReaction))
    }

    "게시물 좋아요 반응 생성 유효성 검사가 실패하면 예외가 발생해야하고 이벤트가 발행되어서는 안된다 " {
        // given
        val postLikeReaction = createPostLikeReaction(status = PostLikeReactionStatus.CANCELED)

        // when then
        shouldThrowAny { postLikeReaction.create(validator = exceptionValidator) }
        postLikeReaction.publishedEvents.shouldBeEmpty()
    }

    "게시물 좋아요 반응이 취소되면 상태는 CANCELED 이어야 한다" {
        // given
        val postLikeReaction = createPostLikeReaction(status = PostLikeReactionStatus.CANCELED)

        // when
        postLikeReaction.cancel()

        // then
        postLikeReaction.status shouldBe PostLikeReactionStatus.CANCELED
    }

    "게시물 좋아요 반응이 취소되면 상태는 CANCELED 이외의 값이 될 수 없다" {
        // given when then
        shouldThrowExactly<IllegalStateException> {
            createPostLikeReaction(status = PostLikeReactionStatus.LIKED).cancel()
        }
    }
})