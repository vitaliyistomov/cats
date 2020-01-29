package de.istomov.cats.domain

import de.istomov.cats.domain.model.Cat
import io.reactivex.Observable
import retrofit2.http.GET

interface CatsRemoteDataSource {

    @GET("v1/images/search")
    fun catUrl(): Observable<List<Cat>>
}