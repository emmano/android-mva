package me.emmano.androidmva.comics.repo

import com.marvel.api.ComicDataWrapper
import me.emmano.androidmva.comics.api.MarvelService
import me.emmano.androidmva.comics.mvvm.ComicModel
import me.emmano.androidmva.comics.mvvm.ComicCell
import me.emmano.androidmva.comics.mvvm.LoadingComicsException

class ComicRepository(private val marvelService: MarvelService) {

    @Throws(LoadingComicsException::class)
    suspend fun comics(): List<ComicModel>? = marvelService.comics().toComicModels()

    private fun ComicDataWrapper.toComicModels(): List<ComicModel>? =
        data.run {
        if(this == null) throw LoadingComicsException
            if(results == null) throw LoadingComicsException
        results?.map { comic ->
            with(comic) {
                val image = images?.first()
                val imageUrl = "${image?.path}.${image?.extension}"
                ComicCell(
                    title.orEmpty(),
                    description.orEmpty(),
                    imageUrl.replace("http", "https")
                )
            }
        }
    }

}