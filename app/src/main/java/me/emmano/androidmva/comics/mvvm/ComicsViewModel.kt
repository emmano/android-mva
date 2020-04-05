package me.emmano.androidmva.comics.mvvm

import me.emmano.androidmva.comics.mvvm.ComicsViewModel.State
import me.emmano.state.BaseViewModel
import me.emmano.state.ViewStateProvider

class ComicsViewModel(viewStateProvider: ViewStateProvider<State>) :
    BaseViewModel<State>(viewStateProvider) {

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

