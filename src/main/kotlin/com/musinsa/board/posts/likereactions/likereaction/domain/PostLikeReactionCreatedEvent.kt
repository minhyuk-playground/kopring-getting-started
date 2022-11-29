package com.musinsa.board.posts.likereactions.likereaction.domain

data class PostLikeReactionCreatedEvent(
    val id: Long,
    val reactorId: Long,
    val likedPostId: Long,
    val status: PostLikeReactionStatus
) {
    constructor(postLikeReaction: PostLikeReaction): this(
        postLikeReaction.id,
        postLikeReaction.likeReactor.reactorId,
        postLikeReaction.likedPost.likedPostId,
        postLikeReaction.status
    )
}
