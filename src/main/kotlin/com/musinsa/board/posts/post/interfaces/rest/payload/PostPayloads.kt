package com.musinsa.board.posts.post.interfaces.rest.payload

import com.musinsa.board.posts.post.domain.Author
import com.musinsa.board.posts.post.domain.Content
import com.musinsa.board.posts.post.domain.Post
import com.musinsa.board.posts.post.domain.PostStatus
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class PostPayload(
    val id: Long? = null,

    val author: AuthorPayload? = null,

    @field:Valid
    @field:NotNull(message = "post.content.not-null")
    val content: ContentPayload?,

    val status: PostStatus? = null
) {
    constructor(post: Post) : this(
        id = post.id,
        author = AuthorPayload(post.author),
        content = ContentPayload(post.content),
        status = post.status
    )

    fun createContent(): Content = content!!.toContent()

    fun createAuthor(authorId: Long): Author = author?.toAuthor() ?: AuthorPayload.createAuthor(authorId)
}

data class AuthorPayload(
    @field:NotNull(message = "post.author.id.not-null")
    @field:Positive(message = "post.author.id.positive")
    val id: Long?
) {
    constructor(author: Author) : this(author.authorId)

    fun toAuthor(): Author = createAuthor(id!!)

    companion object {
        fun createAuthor(authorId: Long): Author = Author(authorId)
    }
}

data class ContentPayload(
    @field:NotBlank(message = "post.content.title.not-blank")
    val title: String?,

    val body: String?
) {
    constructor(content: Content) : this(content.title, content.body)

    fun toContent(): Content = Content(title!!, body)
}