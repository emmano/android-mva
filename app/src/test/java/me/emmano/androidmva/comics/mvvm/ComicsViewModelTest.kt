package me.emmano.androidmva.comics.mvvm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.emmano.androidmva.CoroutineTest
import me.emmano.androidmva.comics.repo.ComicRepository
import me.emmano.androidmva.rule.CoroutineTestRule
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ComicsViewModelTest : CoroutineTest {

    @get:Rule
    override val coroutineRule: CoroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `comics updates state on success`() = test {
        val observer = mock<Observer<ComicsViewModel.State>>()
        val models = mock<List<ComicModel>>()

        val comicRepository = mock<ComicRepository> {
            onBlocking { comics() } doReturn models
        }
        val testObject = ComicsViewModel(comicRepository, this)

        testObject.stateLiveData.observeForever(observer)

        testObject.loadComics()

        verify(observer).onChanged(
            ComicsViewModel.State(
                emptyList(),
                loading = true,
                showError = false
            )
        )

        runCurrent()

        verify(observer).onChanged(
            ComicsViewModel.State(
                models,
                loading = false,
                showError = false
            )
        )
    }

    @Test
    fun `comics updates state on error`() = test {
        val observer = mock<Observer<ComicsViewModel.State>>()

        val comicRepository = mock<ComicRepository> {
            onBlocking { comics() } doThrow LoadingComicsException
        }

        pauseDispatcher()
        val testObject = ComicsViewModel(comicRepository, this)

        testObject.stateLiveData.observeForever(observer)

        testObject.loadComics()

        verify(observer).onChanged(
            ComicsViewModel.State(
                emptyList(),
                loading = true,
                showError = false
            )
        )

        runCurrent()

        verify(observer).onChanged(
            ComicsViewModel.State(
                emptyList(),
                loading = false,
                showError = true
            )
        )
    }
}
