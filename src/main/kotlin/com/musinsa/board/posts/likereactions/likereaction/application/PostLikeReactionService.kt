package com.musinsa.board.posts.likereactions.likereaction.application

import com.musinsa.board.posts.likereactions.likereaction.domain.PostLikeReaction
import com.musinsa.board.posts.likereactions.likereaction.domain.PostLikeReactionRepository
import com.musinsa.board.posts.likereactions.likereaction.domain.PostLikeReactionValidator
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostLikeReactionService(
    private val postLikeReactionRepository: PostLikeReactionRepository,
    private val postLikeReactionCreationValidator: PostLikeReactionValidator
) {
    fun getAllPostLikeReactions(pageable: Pageable): Page<PostLikeReaction> =
        postLikeReactionRepository.findAll(pageable)

    fun getPostLikeReaction(id: Long): PostLikeReaction = postLikeReactionRepository.findById(id)

    @Transactional
    fun create(newPostLikeReaction: PostLikeReaction): PostLikeReaction {
        newPostLikeReaction.create(postLikeReactionCreationValidator)
        return postLikeReactionRepository.save(newPostLikeReaction)
    }

    @Transactional
    fun cancel(id: Long) {
        val postLikeReaction = postLikeReactionRepository.findById(id)
        postLikeReaction.cancel()
    }
}