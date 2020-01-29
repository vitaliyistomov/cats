package de.istomov.cats

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import de.istomov.cats.domain.CatsRemoteDataSource
import de.istomov.cats.domain.CatsRepository
import de.istomov.cats.ui.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

data class CatsApiToken(val token: String)

val catsModule = module {

    single { CatsApiToken(token = "2c5c1b9a-f5dc-411e-89e1-a29ce15556dd") }
    single { CatsRepository(get()) }
    single { get<Retrofit>().create(CatsRemoteDataSource::class.java) }

    single {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    single {
        OkHttpClient.Builder()
            .addNetworkInterceptor(get<HttpLoggingInterceptor>())
            .addNetworkInterceptor { chain ->
                chain.request().newBuilder()
                    .addHeader(name = "x-api-key", value = get<CatsApiToken>().token)
                    .build()
                    .let { chain.proceed(it) }
            }
            .build()
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        Moshi.Builder()
            // ... add your own JsonAdapters and factories ...
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        Picasso.Builder(get())
            .downloader(OkHttp3Downloader(get<OkHttpClient>()))
            .build()
    }

    viewModel { MainViewModel(get()) }
}
