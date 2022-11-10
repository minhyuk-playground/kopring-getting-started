package com.musinsa.board.posts.post.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostRepository : JpaRepository<Post, Long> {
    @Query("select p from Post p where p._status <> :status")
    fun findAllByStatusIsNot(
        status: PostStatus,
        pageable: Pageable
    ): Page<Post>
}