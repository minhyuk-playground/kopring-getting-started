package com.musinsa.board.posts.likereactions.notification.domain

import com.musinsa.board.sharing.domain.LongIdEntity
import com.musinsa.board.sharing.domain.LongIdEntity.Companion.DEFAULT_ID
import javax.persistence.*

@Entity
class PostLikeReactionNotification(
    val postLikeReactionId: Long,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private var _status: PostLikeReactionNotificationStatus,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = DEFAULT_ID
) : LongIdEntity {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PostLikeReactionNotification) return false
        if (postLikeReactionId != other.postLikeReactionId) return false
        return true
    }

    override fun hashCode(): Int = postLikeReactionId.hashCode()

    override fun toString(): String =
        "PostLikeReactionNotification(postLikeReactionId=$postLikeReactionId, _status=$_status, id=$id)"
}