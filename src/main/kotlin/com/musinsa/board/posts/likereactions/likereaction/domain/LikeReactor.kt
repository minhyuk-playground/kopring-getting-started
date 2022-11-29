package com.musinsa.board.posts.likereactions.likereaction.domain

import javax.persistence.Embeddable

@Embeddable
data class LikeReactor(
    val reactorId: Long
) {
    init {
        require(reactorId > 0) { "reactorId can not be less than or equal to zero" }
    }
}
