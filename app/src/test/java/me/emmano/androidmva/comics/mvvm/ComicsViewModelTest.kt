package me.emmano.androidmva.comics.mvvm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.emmano.androidmva.base.BaseTest
import me.emmano.androidmva.comics.repo.ComicRepository
import org.junit.Rule
import org.junit.Test
import org.koin.core.inject

@ExperimentalCoroutinesApi
class ComicsViewModelTest : BaseTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val comicsRepository: ComicRepository by inject()

    @Test
    fun `comics updates state on success`() = test {
        val observer = mock<Observer<ComicsViewModel.State>>()
        val models = mock<List<ComicModel>>()

        whenever(comicsRepository.comics()) doReturn models

        val testObject = ComicsViewModel(this)

        testObject.combinedState.observeForever(observer)

        testObject.loadComics()

        verify(observer).onChanged(
            ComicsViewModel.State(
                emptyList(),
                loading = true,
                showError = false
            )
        )


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

        whenever(comicsRepository.comics()) doThrow LoadingComicsException

        val testObject = ComicsViewModel(this)

        testObject.combinedState.observeForever(observer)

        testObject.loadComics()

        verify(observer).onChanged(
            ComicsViewModel.State(
                emptyList(),
                loading = false,
                showError = true
            )
        )

    }
}
