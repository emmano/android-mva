package me.emmano.androidmva.comics.mvvm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.emmano.androidmva.CoroutineTest
import me.emmano.androidmva.comics.repo.ComicRepository
import me.emmano.androidmva.rule.CoroutineTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.koin.test.mock.declare

@ExperimentalCoroutinesApi
class ComicsViewModelTest : CoroutineTest, AutoCloseKoinTest() {

    @get:Rule
    override val coroutineRule: CoroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val comicsRepository: ComicRepository by inject()

    init {
        startKoin {}
    }

    @Before
    fun setUp() {

        declare { module { single { mock<ComicRepository>() } } }
    }

    @Test
    fun `comics updates state on success`() = test {
        val observer = mock<Observer<ComicsViewModel.State>>()
        val models = mock<List<ComicModel>>()

//        val comicRepository = mock<ComicRepository> {
//            onBlocking { comics() } doReturn models
//        }
//
        whenever(comicsRepository.comics()) doReturn models

        val testObject = ComicsViewModel()

        testObject.combinedState.observeForever(observer)

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

//        val comicRepository = mock<ComicRepository> {
//            onBlocking { comics() } doThrow LoadingComicsException
//        }

        whenever(comicsRepository.comics()) doThrow LoadingComicsException

        pauseDispatcher()
        val testObject = ComicsViewModel()

        testObject.combinedState.observeForever(observer)

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
