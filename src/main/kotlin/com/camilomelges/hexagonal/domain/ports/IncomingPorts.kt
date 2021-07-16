package com.camilomelges.hexagonal.domain.ports

import reactor.core.publisher.Mono

fun interface SavePort<T> {
    fun save(entity: T): Mono<T>
}

fun interface GetPort<T> {
    fun getById(id: String): Mono<T>
}