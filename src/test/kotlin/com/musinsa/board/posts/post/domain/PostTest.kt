package com.musinsa.board.posts.post.domain

import com.musinsa.board.posts.post.exception.IncorrectAuthorException
import com.musinsa.board.posts.post.exception.PostNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

/**
 * 시용자는 게시물을 발행할 수 있다.
 * 사용자는 게시물을 수정할 수 있다.
 * 작성자 본인만 게시물을 수정할 수 있다.
 * 삭제된 게시물은 수정할 수 없다.
 * 작성자는 게시물을 삭제할 수 있다.
 * 작성자 본인만 게시물을 삭제할 수 있다.
 * 이미 삭제된 게시물은 삭제할 수 없다.
 * */
internal class PostTest {
    @Test
    internal fun `게시물 발행`() {
        // given
        val author = Author(authorId = 1L)
        val content = Content("kopring getting started", "hello kopring")
        val status = PostStatus.PUBLISHED

        // when
        val post = createPost(
            author = author,
            content = content,
            status = status
        ).apply { publish() }

        // then
        assertThat(post.id).isZero
        assertThat(post.author).isEqualTo(author)
        assertThat(post.content).isEqualTo(content)
        assertThat(post.status).isEqualTo(status)
    }

    @Test
    internal fun `게시물 발행시 status 는 PUBLISHED 이어야 한다`() {
        //given
        val post = createPost(
            author = Author(authorId = 1L),
            content = Content("kopring getting started", "hello kopring"),
            status = PostStatus.DELETED
        )

        // when then
        assertThatThrownBy {
            post.publish()
        }.isExactlyInstanceOf(IllegalStateException::class.java)
    }

    @Test
    internal fun `게시물 발행시 id 값이 0이 아니라면 예외가 발생해야한다`() {
        // given
        val post = createPost(
            author = Author(authorId = 1L),
            content = Content("kopring getting started", "hello kopring"),
            status = PostStatus.PUBLISHED,
            id = 1L
        )

        // when then
        assertThatThrownBy {
            post.publish()
        }.isExactlyInstanceOf(IllegalStateException::class.java)
    }

    @Test
    internal fun `게시물 수정`() {
        // given
        val newContent = Content("newTitle", "newBody")
        val post = createPost()

        // when
        post.modify(newContent, post.author)

        // then
        assertThat(post.content).isEqualTo(newContent)
    }

    @Test
    internal fun `삭제된 게시물은 수정할 수 없다`() {
        // given
        val post = createPost(status = PostStatus.DELETED)

        // when then
        assertThatThrownBy {
            post.modify(
                content = Content(title = "newTitle", body = "newBody"),
                performer = post.author
            )
        }.isExactlyInstanceOf(PostNotFoundException::class.java)
    }

    @Test
    internal fun `게시물 수정은 작성자 본인만 수행할 수 있다`() {
        // given
        val post = createPost()
        val newContent = Content("newTitle", "newBody")
        val performer = Author(post.author.authorId + 1)

        // when then
        assertThatThrownBy {
            post.modify(newContent, performer)
        }.isExactlyInstanceOf(IncorrectAuthorException::class.java)
    }

    @Test
    internal fun `게시물 삭제`() {
        // given
        val post = createPost(
            author = Author(authorId = 1L),
            content = Content(title = "kopring getting started", body = "hello kopring"),
            status = PostStatus.PUBLISHED
        )

        // when
        post.delete(post.author)

        // then
        assertThat(post.status).isEqualTo(PostStatus.DELETED)
    }

    @Test
    internal fun `작성자 본인만 게시물을 삭제할 수 있다`() {
        // given
        val author = Author(authorId = 1L)
        val performer = Author(authorId = author.authorId + 1)

        val post = createPost(
            author = author,
            content = Content(title = "kopring getting started", body = "hello kopring"),
            status = PostStatus.PUBLISHED
        )

        // when then
        assertThatThrownBy {
            post.delete(performer)
        }.isExactlyInstanceOf(IncorrectAuthorException::class.java)
    }

    @Test
    internal fun `이미 삭제된 게시물은 삭제할 수 없다`() {
        // given
        val post = createPost(
            author = Author(authorId = 1L),
            content = Content(title = "kopring getting started", body = "hello kopring"),
            status = PostStatus.DELETED
        )

        // when then
        assertThatThrownBy {
            post.delete(post.author)
        }.isExactlyInstanceOf(PostNotFoundException::class.java)
    }

    private fun createPost(
        author: Author = Author(authorId = 1L),
        content: Content = Content(title = "title", body = "body"),
        status: PostStatus = PostStatus.PUBLISHED,
        id: Long = 0L
    ): Post = Post(
        author = author,
        _content = content,
        _status = status,
        id = id
    )
}