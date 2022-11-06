package com.musinsa.board.posts.post.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
    fun findAllByStatusIsNot(
        status: PostStatus,
        pageable: Pageable
    ): Page<Post>
}