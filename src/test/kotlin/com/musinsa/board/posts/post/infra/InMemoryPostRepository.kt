package com.musinsa.board.posts.post.infra

import com.musinsa.board.posts.post.domain.Post
import com.musinsa.board.posts.post.domain.PostRepository
import com.musinsa.board.posts.post.domain.PostStatus
import com.musinsa.board.posts.post.exception.PostNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class InMemoryPostRepository : PostRepository {
    private val posts: MutableMap<Long, Post> = hashMapOf()

    override fun save(post: Post): Post {
        if (post.id == 0L) {
            val newPost = Post(author = post.author, _content = post.content, _status = post.status, id = 1L)
            posts[newPost.id] = newPost
            return newPost
        }
        posts[post.id] = post
        return post
    }

    override fun findById(id: Long): Post = posts[id] ?: throw PostNotFoundException()

    override fun existsById(id: Long): Boolean = posts.keys.contains(id)

    override fun findAllByStatusIsNot(status: PostStatus, pageable: Pageable): Page<Post> =
        posts.values
            .asSequence()
            .filter { it.status != status }
            .chunked(pageable.pageSize)
            .elementAt(pageable.pageNumber)
            .let { PageImpl(it, pageable, posts.keys.size.toLong()) }
}