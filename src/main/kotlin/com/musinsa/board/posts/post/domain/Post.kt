package com.musinsa.board.posts.post.domain

import com.musinsa.board.posts.post.exception.IncorrectAuthorException
import com.musinsa.board.posts.post.exception.PostNotFoundException
import javax.persistence.*

@Entity
class Post(
    @Embedded
    val author: Author,

    @Embedded
    private var _content: Content,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private var _status: PostStatus,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = DEFAULT_ID
) {
    val content: Content get() = _content
    val status: PostStatus get() = _status

    fun publish() {
        check(status == PostStatus.PUBLISHED) { "status must be ${PostStatus.PUBLISHED.name}" }
        check(id == DEFAULT_ID) { "id must be default. default id: $DEFAULT_ID" }
    }

    fun modify(content: Content, performer: Author) {
        checkThatStatusIsNotDeleted()
        checkThatAuthorIsCorrect(performer)
        _content = content
    }

    fun delete(performer: Author) {
        checkThatStatusIsNotDeleted()
        checkThatAuthorIsCorrect(performer)
        _status = PostStatus.DELETED
    }

    private fun checkThatAuthorIsCorrect(performer: Author) {
        if (author != performer) {
            throw IncorrectAuthorException(author, performer)
        }
    }

    private fun checkThatStatusIsNotDeleted() {
        if (status == PostStatus.DELETED) {
            throw PostNotFoundException()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Post) return false
        if (author != other.author) return false
        if (_content != other._content) return false
        if (_status != other._status) return false
        return true
    }

    override fun hashCode(): Int {
        var result = author.hashCode()
        result = 31 * result + _content.hashCode()
        result = 31 * result + _status.hashCode()
        return result
    }

    override fun toString(): String = "Post(author=$author, _content=$_content, _status=$_status)"

    companion object {
        private const val DEFAULT_ID = 0L
    }
}