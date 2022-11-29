package com.musinsa.board.posts.likereactions.likereaction.domain

import javax.persistence.Embeddable

@Embeddable
data class LikedPost(
    val likedPostId: Long
) {
    init {
        require(likedPostId > 0) { "likedPostId can not be less than or equal to zero" }
    }
}
