package com.musinsa.board.posts.likereactions.likereaction.infra

import com.musinsa.board.posts.likereactions.likereaction.domain.LikeReactor
import com.musinsa.board.posts.likereactions.likereaction.domain.LikedPost
import com.musinsa.board.posts.likereactions.likereaction.domain.PostLikeReaction
import com.musinsa.board.posts.likereactions.likereaction.domain.PostLikeReactionRepository
import com.musinsa.board.posts.likereactions.likereaction.exception.PostLikeReactionNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class InMemoryPostLikeReactionRepository : PostLikeReactionRepository {
    private val postLikeReactions: MutableMap<Long, PostLikeReaction> = hashMapOf()

    override fun save(postLikeReaction: PostLikeReaction): PostLikeReaction {
        if (postLikeReaction.id == 0L) {
            val newPostLikeReaction =
                PostLikeReaction(postLikeReaction.likeReactor, postLikeReaction.likedPost, postLikeReaction.status, 1L)
            postLikeReactions[newPostLikeReaction.id] = newPostLikeReaction
            return newPostLikeReaction
        }
        postLikeReactions[postLikeReaction.id] = postLikeReaction
        return postLikeReaction
    }

    override fun findById(id: Long): PostLikeReaction =
        postLikeReactions[id] ?: throw PostLikeReactionNotFoundException()

    override fun findAll(pageable: Pageable): Page<PostLikeReaction> =
        postLikeReactions.values
            .asSequence()
            .chunked(pageable.pageSize)
            .elementAt(pageable.pageNumber)
            .let { PageImpl(it, pageable, postLikeReactions.keys.size.toLong()) }

    override fun existsByLikeReactorAndLikedPost(likeReactor: LikeReactor, likedPost: LikedPost): Boolean =
        postLikeReactions.values.any { it.likeReactor == likeReactor && it.likedPost == likedPost }
}