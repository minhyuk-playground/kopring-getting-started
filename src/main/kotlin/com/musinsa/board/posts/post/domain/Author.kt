package com.musinsa.board.posts.post.domain

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Author(
    @Column(name = "author_id")
    val authorId: Long
) {
    init {
        require(value = authorId > 0) { "authorId can not be less than 1" }
    }
}
