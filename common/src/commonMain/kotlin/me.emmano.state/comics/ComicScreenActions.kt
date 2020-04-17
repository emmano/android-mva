package me.emmano.state.comics

import me.emmano.state.AsyncStoreAction
import me.emmano.state.SyncStoreAction

object Loading : SyncStoreAction<ComicsViewModel.State> {
    override fun fire() = { state: ComicsViewModel.State -> state.copy(loading = true, showError = false) }
}

class LoadComics :
    AsyncStoreAction<ComicsViewModel.State> {

    private val comicRepository  = ComicsRepository(engine)

    override suspend fun fire(currentState: ComicsViewModel.State): (ComicsViewModel.State) -> ComicsViewModel.State {
        val comics = comicRepository.getComics()
        return {state: ComicsViewModel.State -> state.copy(comics, false, false)}
    }
}

val ShowError = { state: ComicsViewModel.State -> state.copy(loading = false, showError = true) }