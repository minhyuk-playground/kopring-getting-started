package com.musinsa.board.posts.likereactions.likereaction.domain

import com.musinsa.board.sharing.domain.AggregateRoot
import com.musinsa.board.sharing.domain.LongIdEntity
import com.musinsa.board.sharing.domain.LongIdEntity.Companion.DEFAULT_ID
import javax.persistence.*

@Entity
class PostLikeReaction(
    @Embedded
    val likeReactor: LikeReactor,

    @Embedded
    val likedPost: LikedPost,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private var _status: PostLikeReactionStatus,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = DEFAULT_ID,
) : AggregateRoot<PostLikeReaction>(), LongIdEntity {
    val status get() = _status

    fun create(validator: PostLikeReactionValidator) {
        validator.validate(this)
        registerEvent(PostLikeReactionCreatedEvent(this))
    }

    fun cancel() {
        check(status == PostLikeReactionStatus.CANCELED) { "status must be ${PostLikeReactionStatus.CANCELED}" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PostLikeReaction) return false
        if (likeReactor != other.likeReactor) return false
        if (likedPost != other.likedPost) return false
        return true
    }

    override fun hashCode(): Int {
        var result = likeReactor.hashCode()
        result = 31 * result + likedPost.hashCode()
        return result
    }
}