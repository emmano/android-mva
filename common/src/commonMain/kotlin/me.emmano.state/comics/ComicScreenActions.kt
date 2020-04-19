package me.emmano.state.comics

import me.emmano.state.AsyncAction
import me.emmano.state.SyncAction

object Loading : SyncAction<ComicsViewModel.State> {
    override fun fire() = { state: ComicsViewModel.State -> state.copy(loading = true, showError = false) }
}

class LoadComics :
    AsyncAction<ComicsViewModel.State> {

    private val comicRepository  = ComicsRepository(engine)

    override suspend fun fire(currentState: ComicsViewModel.State): (ComicsViewModel.State) -> ComicsViewModel.State {
        val comics = comicRepository.getComics()
        return {state: ComicsViewModel.State -> state.copy(comics, false, false)}
    }
}

val ShowError = { state: ComicsViewModel.State -> state.copy(loading = false, showError = true) }