package com.musinsa.board.posts.likereactions.likereaction.interfaces.rest

import com.musinsa.board.posts.likereactions.likereaction.application.PostLikeReactionService
import com.musinsa.board.posts.likereactions.likereaction.interfaces.rest.payload.PostLikeReactionPayload
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/drafts")
class PostLikeReactionRestController(
    private val postLikeReactionService: PostLikeReactionService
) {
    @GetMapping
    fun getAllPostLikeReactions(
        pageable: Pageable
    ): ResponseEntity<Page<PostLikeReactionPayload>> =
        postLikeReactionService.getAllPostLikeReactions(pageable).let {
            ResponseEntity.ok(it.map { reaction -> PostLikeReactionPayload(reaction) })
        }

    @GetMapping("/{id}")
    fun getPostLikeReaction(
        @PathVariable id: Long
    ): ResponseEntity<PostLikeReactionPayload> =
        postLikeReactionService.getPostLikeReaction(id).let {
            ResponseEntity.ok(PostLikeReactionPayload(it))
        }

    @PostMapping
    fun create(
        @RequestBody payload: PostLikeReactionPayload
    ): ResponseEntity<PostLikeReactionPayload> =
        postLikeReactionService.create(payload.toPostLikeReaction()).let {
            ResponseEntity.created(URI.create("/drafts/${it.id}")).body(PostLikeReactionPayload(it))
        }

    @DeleteMapping("/{id}")
    fun cancel(
        @PathVariable id: Long
    ): ResponseEntity<Unit> =
        postLikeReactionService.cancel(id).let { ResponseEntity.ok().build() }
}