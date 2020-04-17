package me.emmano.state.comics

import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.Logger
import me.emmano.state.BaseViewModel
import me.emmano.state.ViewStateProvider

class ComicsViewModel(viewStateProvider: ViewStateProvider<State>) :
    BaseViewModel<ComicsViewModel.State>(viewStateProvider) {

    override val errors = { t: Throwable ->
        Logger.DEFAULT.log("ERROR: ${t.message}")
        ShowError
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