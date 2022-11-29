package com.musinsa.board.posts.likereactions.likereaction.infra

import com.musinsa.board.posts.likereactions.likereaction.domain.LikeReactor
import com.musinsa.board.posts.likereactions.likereaction.domain.LikedPost
import com.musinsa.board.posts.likereactions.likereaction.domain.PostLikeReaction
import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataJpaPostLikeReactionRepository : JpaRepository<PostLikeReaction, Long> {
    fun existsByLikeReactorAndLikedPost(likeReactor: LikeReactor, likedPost: LikedPost): Boolean
}