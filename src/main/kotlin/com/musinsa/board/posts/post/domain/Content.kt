package com.musinsa.board.posts.post.domain

import javax.persistence.Embeddable

/**
 * 게시물은 반드시 1글자 이상의 제목을 가지고 있어야 한다.
 * 게시물의 제목이 빈문자열이라면 게시물을 저장할 수 없다.
 * 게시물의 본문 내용은 비어있을 수 있다.
 * */
@Embeddable
data class Content(
    val title: String,
    val body: String? = null,
) {
    init {
        require(title.isNotBlank()) { "title can not be blank string" }
    }
}
