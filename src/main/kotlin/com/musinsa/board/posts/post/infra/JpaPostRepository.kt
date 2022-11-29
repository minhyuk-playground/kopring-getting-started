package com.musinsa.board.posts.post.infra

import com.musinsa.board.posts.post.domain.Post
import com.musinsa.board.posts.post.domain.PostRepository
import com.musinsa.board.posts.post.domain.PostStatus
import com.musinsa.board.posts.post.exception.PostNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class JpaPostRepository(
    private val repository: SpringDataJpaPostRepository
) : PostRepository {

    override fun save(post: Post): Post = repository.save(post)

    override fun findById(id: Long): Post = repository.findByIdOrNull(id) ?: throw PostNotFoundException()

    override fun existsById(id: Long): Boolean = repository.existsById(id)

    override fun findAllByStatusIsNot(status: PostStatus, pageable: Pageable): Page<Post> =
        repository.findAllByStatusIsNot(status, pageable)
}