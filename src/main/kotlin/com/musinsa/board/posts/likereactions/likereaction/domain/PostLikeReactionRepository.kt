package com.musinsa.board.posts.likereactions.likereaction.domain

import com.musinsa.board.posts.likereactions.likereaction.exception.PostLikeReactionNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import kotlin.jvm.Throws

interface PostLikeReactionRepository {
    fun save(postLikeReaction: PostLikeReaction): PostLikeReaction

    @Throws(PostLikeReactionNotFoundException::class)
    fun findById(id: Long): PostLikeReaction

    fun findAll(pageable: Pageable): Page<PostLikeReaction>

    fun existsByLikeReactorAndLikedPost(likeReactor: LikeReactor, likedPost: LikedPost): Boolean
}