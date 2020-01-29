package de.istomov.cats.domain

import io.reactivex.Observable

class CatsRepository(
    private val catsRemoteDataSource: CatsRemoteDataSource
) {

    fun catUrl(): Observable<String> {
        return catsRemoteDataSource.catUrl()
            .flatMapIterable { it }
            .firstElement()
            .toObservable()
            .map { it.url }
    }
}