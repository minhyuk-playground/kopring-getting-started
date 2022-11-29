package com.musinsa.board.posts.likereactions.notification.infra

import com.musinsa.board.posts.likereactions.notification.domain.PostLikeReactionNotification
import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataJpaPostLikeReactionNotificationRepository : JpaRepository<PostLikeReactionNotification, Long>