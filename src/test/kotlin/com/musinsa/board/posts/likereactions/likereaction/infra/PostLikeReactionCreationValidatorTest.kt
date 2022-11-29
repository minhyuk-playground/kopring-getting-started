package com.musinsa.board.posts.likereactions.likereaction.infra

import com.musinsa.board.posts.likereactions.likereaction.domain.*
import com.musinsa.board.posts.likereactions.likereaction.exception.LikedPostNotFoundException
import com.musinsa.board.posts.likereactions.likereaction.exception.PostLikeReactionDuplicateException
import com.musinsa.board.posts.post.domain.*
import com.musinsa.board.posts.post.infra.InMemoryPostRepository
import com.musinsa.board.sharing.domain.LongIdEntity
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.matchers.throwable.shouldHaveMessage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PostLikeReactionCreationValidatorTest {
    private lateinit var postRepository: PostRepository
    private lateinit var postLikeReactionRepository: PostLikeReactionRepository
    private lateinit var postLikeReactionCreationValidator: PostLikeReactionCreationValidator

    @BeforeEach
    internal fun setUp() {
        postRepository = InMemoryPostRepository()
        postLikeReactionRepository = InMemoryPostLikeReactionRepository()
        postLikeReactionCreationValidator = PostLikeReactionCreationValidator(
            postRepository,
            postLikeReactionRepository
        )
    }

    @Test
    internal fun `유효성 검사 성공`() {
        // given
        val post = savePost()
        val postLikeReaction = createPostLikeReaction(
            likeReactor = LikeReactor(post.author.authorId),
            likedPost = LikedPost(post.id)
        )

        // when then
        shouldNotThrowAny { postLikeReaction.create(postLikeReactionCreationValidator) }
    }

    @Test
    internal fun `PostLikeReaction 이 새롭게 생성된 것이 아니라면 예외가 발생해야 한다`() {
        // given
        val postLikeReaction = createPostLikeReaction(id = 1L)

        // when then
        shouldThrowExactly<IllegalStateException> {
            postLikeReaction.create(validator = postLikeReactionCreationValidator)
        } shouldHaveMessage ("id can not be default. current id : ${postLikeReaction.id}")
    }

    @Test
    internal fun `status 가 LIKED 가 아니라면 예외가 발생해야 한다`() {
        // given
        val postLikeReaction = createPostLikeReaction(postLikeReactionStatus = PostLikeReactionStatus.CANCELED)

        // when then
        shouldThrowExactly<IllegalStateException> {
            postLikeReaction.create(validator = postLikeReactionCreationValidator)
        } shouldHaveMessage ("status must be LIKED. current status : ${postLikeReaction.status}")
    }

    @Test
    internal fun `Post 가 존재하지 않을 경우 예외가 발생해야 한다`() {
        // given
        val postLikeReaction = createPostLikeReaction()

        // when then
        shouldThrowExactly<LikedPostNotFoundException> {
            postLikeReaction.create(validator = postLikeReactionCreationValidator)
        }
    }

    @Test
    internal fun `발행된 포스트가 아니라면 예외가 발생해야한다`() {
        // given
        val post = savePost(postStatus = PostStatus.DELETED)

        val postLikeReaction = createPostLikeReaction(
            likeReactor = LikeReactor(post.author.authorId),
            likedPost = LikedPost(post.id)
        )

        // when then
        shouldThrowExactly<LikedPostNotFoundException> {
            postLikeReaction.create(validator = postLikeReactionCreationValidator)
        }
    }

    @Test
    internal fun `이미 존재하는 PostLikeReaction 일 경우 예외가 발생해야 한다`() {
        // given
        val post = savePost()
        val postLikeReaction = createPostLikeReaction(
            likeReactor = LikeReactor(post.author.authorId),
            likedPost = LikedPost(post.id)
        )

        postLikeReactionRepository.save(postLikeReaction)

        // when then
        shouldThrowExactly<PostLikeReactionDuplicateException> {
            postLikeReaction.create(validator = postLikeReactionCreationValidator)
        }
    }

    private fun createPostLikeReaction(
        likeReactor: LikeReactor = LikeReactor(1L),
        likedPost: LikedPost = LikedPost(1L),
        postLikeReactionStatus: PostLikeReactionStatus = PostLikeReactionStatus.LIKED,
        id: Long = LongIdEntity.DEFAULT_ID
    ): PostLikeReaction =
        PostLikeReaction(
            likeReactor = likeReactor,
            likedPost = likedPost,
            _status = postLikeReactionStatus,
            id = id
        )

    private fun savePost(
        author: Author = Author(1L),
        content: Content = Content("title", "body"),
        postStatus: PostStatus = PostStatus.PUBLISHED
    ): Post = postRepository.save(Post(author, content, postStatus))
}