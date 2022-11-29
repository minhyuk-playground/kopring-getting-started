package com.musinsa.board.posts.likereactions.notification.domain

interface PostLikeReactionNotificationSender {
    fun sendNotification(trial: PostLikeReactionNotificationTrial): PostLikeReactionNotification
}