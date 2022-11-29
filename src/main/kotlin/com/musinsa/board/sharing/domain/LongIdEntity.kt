package com.musinsa.board.sharing.domain

interface LongIdEntity: EntityPersistable {
    val id: Long

    override fun isNew(): Boolean = id == DEFAULT_ID

    companion object {
        const val DEFAULT_ID = 0L
    }
}