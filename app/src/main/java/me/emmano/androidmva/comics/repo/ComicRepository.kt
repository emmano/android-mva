package me.emmano.androidmva.comics.repo

import com.marvel.api.ComicDataWrapper
import me.emmano.androidmva.comics.api.MarvelService
import me.emmano.androidmva.comics.mvvm.ComicModel
import me.emmano.androidmva.comics.mvvm.LoadingComicsException
import javax.inject.Inject

class ComicRepository @Inject constructor(private val marvelService: MarvelService) {

    @Throws(LoadingComicsException::class)
    suspend fun comics(): List<ComicModel>? = marvelService.comics().toComicModels()

    private fun ComicDataWrapper.toComicModels(): List<ComicModel>? =
        data.run {
        if(this == null) throw LoadingComicsException
            if(results == null) throw LoadingComicsException
        results?.map { comic ->
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