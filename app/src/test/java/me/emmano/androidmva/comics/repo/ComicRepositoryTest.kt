package me.emmano.androidmva.comics.repo

import com.marvel.api.Comic
import com.marvel.api.ComicDataContainer
import com.marvel.api.ComicDataWrapper
import com.marvel.api.Image
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.emmano.androidmva.CoroutineTest
import me.emmano.androidmva.comics.api.MarvelService
import me.emmano.androidmva.comics.mvvm.ComicCell
import me.emmano.androidmva.comics.mvvm.ComicModel
import me.emmano.androidmva.comics.mvvm.LoadingComicsException
import me.emmano.androidmva.rule.CoroutineTestRule
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ComicRepositoryTest : CoroutineTest{

    @get:Rule
    override val coroutineRule = CoroutineTestRule()

    @Test
    fun `comics delegates to marvel service`() = test{
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
            onBlocking { comics() } doReturn comicDataWrapper
        }

        val testObject = ComicRepository(marvelService)

        val comics = testObject.comics()

        val comicModels: List<ComicModel> = listOf(ComicCell("title", "description", "https://path.jpg"))
        assertThat(comics, equalTo(comicModels))
    }

    @Test(expected = LoadingComicsException::class)
    fun `throw if data is null`() = test {
        val marvelService = mock<MarvelService> {
            val comicDataWrapper = mock<ComicDataWrapper> {
                on { data } doReturn null
            }
            onBlocking { comics() } doReturn comicDataWrapper
        }

        val testObject = ComicRepository(marvelService)

        testObject.comics()
    }
}