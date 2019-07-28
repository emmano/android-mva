package me.emmano.androidmva.comics.repo

import com.marvel.api.Comic
import com.marvel.api.ComicDataContainer
import com.marvel.api.ComicDataWrapper
import com.marvel.api.Image
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import me.emmano.androidmva.comics.api.MarvelService
import me.emmano.androidmva.comics.mvvm.ComicModel
import org.junit.Test

class ComicRepositoryTest {

    @Test
    fun `comics delegates to marvel service`() {
        val marvelService = mock<MarvelService> {
            val comicDataContainer = mock<ComicDataContainer> {
                on { results } doReturn listOf(
                    Comic(
                        title = "title",
                        description = "description",
                        images = listOf(Image("http://path", "jpg"))
                    )
                )
            }
            val comicDataWrapper = mock<ComicDataWrapper> {
                on { data } doReturn comicDataContainer
            }
            on { comics() } doReturn Single.just(comicDataWrapper)
        }

        val testObject = ComicRepository(marvelService)

        val testObserver = testObject.comics.test()

        testObserver.assertValue(listOf(ComicModel("title", "description", "https://path.jpg")))
    }
}