package me.emmano.androidmva.api

import com.marvel.api.ComicDataWrapper
import io.reactivex.Single
import me.emmano.androidmva.BuildConfig
import retrofit2.http.GET

interface MarvelService {

    @GET("comics?format=comic&formatType=comic&noVariants=true&dateDescriptor=thisMonth&ts=1&apikey=" + BuildConfig.API_KEY)
    fun comics() : Single<ComicDataWrapper>
}