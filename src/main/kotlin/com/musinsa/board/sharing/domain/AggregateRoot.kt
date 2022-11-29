package com.musinsa.board.sharing.domain

import org.springframework.data.domain.AbstractAggregateRoot

abstract class AggregateRoot<T> : AbstractAggregateRoot<T>() where T : AggregateRoot<T>, T : EntityPersistable {
    internal val publishedEvents: List<Any> get() = domainEvents().toList()
}