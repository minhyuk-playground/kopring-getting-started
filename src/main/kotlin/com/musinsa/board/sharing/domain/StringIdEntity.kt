package com.musinsa.board.sharing.domain

interface StringIdEntity: EntityPersistable {
    val id: String?

    override fun isNew(): Boolean = id == null
}