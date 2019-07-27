package me.emmano.androidmva.comics.mvvm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.subjects.SingleSubject
import me.emmano.androidmva.comics.repo.ComicRepository
import org.junit.Rule
import org.junit.Test

class ComicsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `comics updates state on success`() {
        val observer = mock<Observer<ComicsViewModel.State>>()
        val comicsSubject = SingleSubject.create<List<ComicModel>?>()
        val models = mock<List<ComicModel>>()

        val comicRepository = mock<ComicRepository>{
            on { comics } doReturn comicsSubject
        }
        val testObject = ComicsViewModel(comicRepository)

        testObject.stateLiveData.observeForever(observer)

        testObject.loadComics()

        verify(observer).onChanged(ComicsViewModel.State(emptyList(), loading = true, showError = false))

        comicsSubject.onSuccess(models)

        verify(observer).onChanged(ComicsViewModel.State(models, loading = false, showError = false))
    }

    @Test
    fun `comics updates state on error`() {
        val observer = mock<Observer<ComicsViewModel.State>>()
        val comicsSubject = SingleSubject.create<List<ComicModel>?>()

        val comicRepository = mock<ComicRepository>{
            on { comics } doReturn comicsSubject
        }
        val testObject = ComicsViewModel(comicRepository)

        testObject.stateLiveData.observeForever(observer)

        testObject.loadComics()

        verify(observer).onChanged(ComicsViewModel.State(emptyList(), loading = true, showError = false))

        comicsSubject.onError(Throwable())

        verify(observer).onChanged(ComicsViewModel.State(emptyList(), loading = false, showError = true))
    }
}