package me.emmano.state.comics

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import me.emmano.state.comics.api.ComicDataWrapper

class ComicsRepository(clientEngine: HttpClientEngine) {
    private val client = HttpClient(clientEngine)
    {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json(JsonConfiguration(ignoreUnknownKeys = true)))
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
    }

    suspend fun getComics(): List<ComicModel> =
        client.get<ComicDataWrapper>("https://gateway.marvel.com/v1/public/comics?limit=40&format=comic&formatType=comic&noVariants=true&ts=1&apikey=9d40db1630da003c1fb826d96bef65eb&hash=b167f09767dfce350bc36a144429c1b5")
            .toComicModels()

    private fun ComicDataWrapper.toComicModels(): List<ComicModel> =
        data.run {
            if(this == null) throw IllegalStateException()
            if(results == null) throw IllegalStateException()
            results.map { comic ->
                with(comic) {
                    val image = thumbnail
                    val imageUrl = "${image?.path}.${image?.extension}"
                    ComicModel(
                        title.orEmpty(),
                        description.orEmpty(),
                        imageUrl.replace("http", "https")
                    )
                }
            }
        }


}