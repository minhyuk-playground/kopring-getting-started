package com.musinsa.board.posts.likereactions.notification.domain

interface PostLikeReactionNotificationRepository {
    fun save(postLikeReactionNotification: PostLikeReactionNotification): PostLikeReactionNotification
    fun findById(id: Long): PostLikeReactionNotification
}