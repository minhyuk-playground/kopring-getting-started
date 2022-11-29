package com.musinsa.board.posts.likereactions.notification.domain

data class PostLikeReactionNotificationTrial(
    val postLikedReactionId: Long,
    val likedPostId: Long,
    val postLikeReactorId: Long
)