package com.musinsa.board.posts.post.domain

import com.musinsa.board.posts.post.exception.PostNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostRepository {
    fun save(post: Post): Post

    @Throws(PostNotFoundException::class)
    fun findById(id: Long): Post

    fun existsById(id: Long): Boolean

    fun findAllByStatusIsNot(status: PostStatus, pageable: Pageable): Page<Post>
}