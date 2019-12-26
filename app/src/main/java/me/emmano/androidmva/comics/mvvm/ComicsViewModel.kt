package me.emmano.androidmva.comics.mvvm

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.emmano.androidmva.base.*
import me.emmano.androidmva.comics.mvvm.ComicsViewModel.State
import me.emmano.androidmva.comics.repo.ComicRepository

class ComicsViewModel(
    private val comicRepository: ComicRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel2<State>(State(), Store()) {

    val comics by observe {
        it.comics
    }
    val loading by observe {
        it.loading
    }

    fun loadComics() {
        action(Loading)
        try {
            action(LoadComics(comicRepository))

        } catch (e: LoadingComicsException) {
            action(ShowError)

        }
    }

    data class State(
        val comics: List<ComicModel> = emptyList(),
        val loading: Boolean = false,
        val showError: Boolean = false
    )

}

