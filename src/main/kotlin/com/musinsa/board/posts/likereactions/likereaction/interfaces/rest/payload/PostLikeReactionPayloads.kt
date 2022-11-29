package com.musinsa.board.posts.likereactions.likereaction.interfaces.rest.payload

import com.musinsa.board.posts.likereactions.likereaction.domain.LikeReactor
import com.musinsa.board.posts.likereactions.likereaction.domain.LikedPost
import com.musinsa.board.posts.likereactions.likereaction.domain.PostLikeReaction
import com.musinsa.board.posts.likereactions.likereaction.domain.PostLikeReactionStatus
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class PostLikeReactionPayload(
    val id: Long?,

    @field:NotNull
    @field:Positive
    val likeReactorId: Long?,

    @field:NotNull
    @field:Positive
    val likedPostId: Long?,

    @field:NotNull
    val status: PostLikeReactionStatus?
) {
    constructor(postLikeReaction: PostLikeReaction) : this(
        postLikeReaction.id,
        postLikeReaction.likeReactor.reactorId,
        postLikeReaction.likedPost.likedPostId,
        postLikeReaction.status
    )

    fun toPostLikeReaction(): PostLikeReaction =
        PostLikeReaction(
            likeReactor = LikeReactor(reactorId = likeReactorId!!),
            likedPost = LikedPost(likedPostId = likedPostId!!),
            _status = status!!
        )
}