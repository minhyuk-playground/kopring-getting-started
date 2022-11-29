package com.musinsa.board.posts.likereactions.likereaction.infra

import com.musinsa.board.posts.likereactions.likereaction.domain.LikeReactor
import com.musinsa.board.posts.likereactions.likereaction.domain.LikedPost
import com.musinsa.board.posts.likereactions.likereaction.domain.PostLikeReaction
import com.musinsa.board.posts.likereactions.likereaction.domain.PostLikeReactionRepository
import com.musinsa.board.posts.likereactions.likereaction.exception.PostLikeReactionNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class JpaPostLikeReactionRepository(
    private val repository: SpringDataJpaPostLikeReactionRepository
) : PostLikeReactionRepository {
    override fun save(postLikeReaction: PostLikeReaction): PostLikeReaction =
        repository.save(postLikeReaction)

    override fun findById(id: Long): PostLikeReaction =
        repository.findByIdOrNull(id) ?: throw PostLikeReactionNotFoundException()

    override fun findAll(
        pageable: Pageable
    ): Page<PostLikeReaction> =
        repository.findAll(pageable)

    override fun existsByLikeReactorAndLikedPost(
        likeReactor: LikeReactor,
        likedPost: LikedPost
    ): Boolean = repository.existsByLikeReactorAndLikedPost(likeReactor, likedPost)
}