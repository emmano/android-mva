package me.emmano.androidmva.base

import me.emmano.androidmva.BuildConfig
import org.koin.dsl.module
import timber.log.Timber

val networkModule = module {

//    single {
//        val httpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
//            override fun log(message: String) {
//                Timber.tag("OkHttp").d(message)
//            }
//        }).apply { level =  HttpLoggingInterceptor.Level.BODY}
//
//        OkHttpClient.Builder()
//            .addNetworkInterceptor(httpLoggingInterceptor)
//            .build()
//    }
//
//    single {
//        Retrofit.Builder()
//            .baseUrl(BuildConfig.BASE_URL)
//            .client(get())
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
}