package com.musinsa.board.posts.likereactions.likereaction.infra

import com.musinsa.board.posts.likereactions.likereaction.domain.*
import com.musinsa.board.posts.likereactions.likereaction.exception.LikedPostNotFoundException
import com.musinsa.board.posts.likereactions.likereaction.exception.PostLikeReactionDuplicateException
import com.musinsa.board.posts.post.domain.PostRepository
import com.musinsa.board.posts.post.domain.PostStatus
import com.musinsa.board.posts.post.exception.PostNotFoundException
import org.springframework.stereotype.Component

@Component
class PostLikeReactionCreationValidator(
    private val postRepository: PostRepository,
    private val postLikeReactionRepository: PostLikeReactionRepository
) : PostLikeReactionValidator {
    override fun validate(postLikeReaction: PostLikeReaction) {
        if (!postLikeReaction.isNew()) {
            throw IllegalStateException("id can not be default. current id : ${postLikeReaction.id}")
        }

        if (postLikeReaction.status != PostLikeReactionStatus.LIKED) {
            throw IllegalStateException("status must be LIKED. current status : ${postLikeReaction.status}")
        }

        validatePost(postLikeReaction.likedPost)
        checkDuplicatedReaction(postLikeReaction)
    }

    private fun validatePost(likedPost: LikedPost) {
        val post = try {
            postRepository.findById(likedPost.likedPostId)
        } catch (e: PostNotFoundException) {
            throw LikedPostNotFoundException()
        }

        if (post.status != PostStatus.PUBLISHED) {
            throw LikedPostNotFoundException()
        }
    }

    private fun checkDuplicatedReaction(newReaction: PostLikeReaction) {
        val existsReaction = postLikeReactionRepository.existsByLikeReactorAndLikedPost(
            likeReactor = newReaction.likeReactor,
            likedPost = newReaction.likedPost
        )

        if (existsReaction) {
            throw PostLikeReactionDuplicateException()
        }
    }
}