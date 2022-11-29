package com.musinsa.board.posts.likereactions.notification.infra

import com.musinsa.board.posts.likereactions.notification.domain.PostLikeReactionNotification
import com.musinsa.board.posts.likereactions.notification.domain.PostLikeReactionNotificationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class JpaPostLikeReactionNotificationRepository(
    private val repository: SpringDataJpaPostLikeReactionNotificationRepository
) : PostLikeReactionNotificationRepository {
    override fun save(postLikeReactionNotification: PostLikeReactionNotification): PostLikeReactionNotification =
        repository.save(postLikeReactionNotification)

    override fun findById(id: Long): PostLikeReactionNotification =
        repository.findByIdOrNull(id) ?: throw NoSuchElementException()
}