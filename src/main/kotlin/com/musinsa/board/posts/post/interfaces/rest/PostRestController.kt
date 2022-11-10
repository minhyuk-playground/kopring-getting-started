package com.musinsa.board.posts.post.interfaces.rest

import com.musinsa.board.posts.post.application.PostService
import com.musinsa.board.posts.post.domain.Author
import com.musinsa.board.posts.post.interfaces.rest.payload.AuthorPayload
import com.musinsa.board.posts.post.interfaces.rest.payload.ContentPayload
import com.musinsa.board.posts.post.interfaces.rest.payload.PostPayload
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/posts")
class PostRestController(
    private val postService: PostService
) {
    @GetMapping
    fun getAllPosts(
        pageable: Pageable,
    ): ResponseEntity<Page<PostPayload>> =
        postService.getAllPosts(pageable)
            .map { PostPayload(it) }
            .let { ResponseEntity.ok(it) }

    @GetMapping("/{postId}")
    fun getPost(
        @PathVariable postId: Long
    ): ResponseEntity<PostPayload> =
        postService.getPost(postId).let { ResponseEntity.ok(PostPayload(it)) }

    @PostMapping
    fun publish(
        @Validated
        @RequestBody postPayload: PostPayload
    ): ResponseEntity<Unit> =
        postService.publish(
            content = postPayload.createContent(),
            performer = postPayload.createAuthor(authorId = DEFAULT_AUTHOR_ID)
        ).let {
            ResponseEntity.created(URI.create("/posts/$it")).build()
        }

    @PatchMapping("/{postId}/content")
    fun modify(
        @PathVariable postId: Long,
        @RequestBody contentPayload: ContentPayload
    ): ResponseEntity<Unit> =
        postService.modify(
            postId = postId,
            content = contentPayload.toContent(),
            performer = AuthorPayload.createAuthor(DEFAULT_AUTHOR_ID)
        ).let {
            ResponseEntity.noContent().build()
        }

    @DeleteMapping("/{postId}")
    fun delete(
        @PathVariable postId: Long
    ): ResponseEntity<Unit> =
        postService.delete(
            id = postId,
            performer = Author(DEFAULT_AUTHOR_ID)
        ).let {
            ResponseEntity.ok().build()
        }

    companion object {
        private const val DEFAULT_AUTHOR_ID: Long = 1L
    }
}