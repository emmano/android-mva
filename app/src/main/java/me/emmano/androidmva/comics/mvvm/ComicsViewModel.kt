package me.emmano.androidmva.comics.mvvm

import me.emmano.androidmva.base.*
import me.emmano.androidmva.comics.mvvm.ComicsViewModel.State

class ComicsViewModel() : BaseViewModel2<State>(State(), Store(State())) {

    val comics by observe {
        it.comics
    }
    val loading by observe {
        it.loading
    }

    fun loadComics() {
        action(Loading)
        try {
            action(LoadComics)

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

