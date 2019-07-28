package me.emmano.androidmva.comics.repo

import com.marvel.api.ComicDataWrapper
import io.reactivex.Single
import me.emmano.androidmva.comics.api.MarvelService
import me.emmano.androidmva.comics.mvvm.ComicModel

class ComicRepository(private val marvelService: MarvelService) {

    val comics: Single<List<ComicModel>?> by lazy { marvelService.comics().toComicModels() }

    private fun Single<ComicDataWrapper>.toComicModels(): Single<List<ComicModel>?> = this.map {
        it.data?.results?.map { comic ->
            with(comic) {

                val image = images?.first()
                val imageUrl = "${image?.path}.${image?.extension}"

                ComicModel(title.orEmpty(), description.orEmpty(), imageUrl.replace("http", "https"))
            }
        }
    }

}