package com.musinsa.board.posts.post.application

import com.musinsa.board.posts.post.domain.*
import com.musinsa.board.posts.post.exception.PostNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository
) {
    fun getAllPosts(
        pageable: Pageable
    ): Page<Post> =
        postRepository.findAllByStatusIsNot(status = PostStatus.DELETED, pageable = pageable)

    fun getPost(postId: Long): Post =
        postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()

    @Transactional
    fun publish(
        content: Content,
        performer: Author
    ): Long =
        Post(
            author = performer,
            _content = content,
            _status = PostStatus.PUBLISHED
        ).apply {
            publish()
        }.let {
            postRepository.save(it).id
        }

    @Transactional
    fun modify(
        postId: Long,
        content: Content,
        performer: Author
    ) = getPost(postId = postId).modify(content = content, performer = performer)

    @Transactional
    fun delete(id: Long, performer: Author) = getPost(postId = id).delete(performer = performer)
}