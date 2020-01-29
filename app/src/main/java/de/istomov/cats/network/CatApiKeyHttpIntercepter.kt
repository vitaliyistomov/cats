//package de.istomov.cats.network
//
//import de.istomov.cats.CatsApiToken
//import okhttp3.Interceptor
//import okhttp3.Response
//
//class CatApiKeyHttpIntercepter() :Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        chain.call().request().newBuilder()
//            .addHeader(name = "x-api-key", value = get<CatsApiToken>().token)
//            .build()
//            .let { chain.proceed(it) }
//
//    }
//
//}
