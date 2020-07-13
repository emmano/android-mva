package me.emmano.androidmva.comics.mvvm

import dagger.hilt.android.scopes.ActivityRetainedScoped
import me.emmano.androidmva.comics.mvvm.ComicsViewModel.State
import me.emmano.state.BaseViewModel
import me.emmano.state.ViewStateProvider
import javax.inject.Inject

@ActivityRetainedScoped
class ComicsViewModel @Inject constructor(viewStateProvider: ViewStateProvider<State>, private val loadComics: LoadComics) :
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
        action(loadComics)
    }

    data class State(
        val comics: List<ComicModel> = emptyList(),
        val loading: Boolean = false,
        val showError: Boolean = false
    )

}

