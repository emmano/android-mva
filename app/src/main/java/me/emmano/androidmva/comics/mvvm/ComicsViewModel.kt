package me.emmano.androidmva.comics.mvvm

import me.emmano.androidmva.base.*
import me.emmano.androidmva.comics.mvvm.ComicsViewModel.State

class ComicsViewModel(store: Store<State>) :
    BaseViewModel<State>(store) {

    override val errors = { t: Throwable ->
        ShowError
    }

    val comics by observe {
        it.comics
    }
    val loading by observe {
        it.loading
    }

    fun loadComics() {
        action(Loading)
        action(LoadComics())
    }

    data class State(
        val comics: List<ComicModel> = emptyList(),
        val loading: Boolean = false,
        val showError: Boolean = false
    )

}

