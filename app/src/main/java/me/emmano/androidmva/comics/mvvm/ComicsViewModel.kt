package me.emmano.androidmva.comics.mvvm

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.emmano.androidmva.base.*
import me.emmano.androidmva.comics.mvvm.ComicsViewModel.State

class ComicsViewModel(dispatcher: CoroutineDispatcher = Dispatchers.IO) : BaseViewModel2<State>(Store(State(), dispatcher)) {

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

