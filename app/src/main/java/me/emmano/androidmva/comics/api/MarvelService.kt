package me.emmano.androidmva.comics.api

import com.marvel.api.ComicDataWrapper
import me.emmano.androidmva.BuildConfig
import retrofit2.http.GET

interface MarvelService {

    @GET("comics?limit=40&format=comic&formatType=comic&noVariants=true&dateDescriptor=thisMonth&ts=1&apikey=" + BuildConfig.API_KEY)
    suspend fun comics() : ComicDataWrapper
}