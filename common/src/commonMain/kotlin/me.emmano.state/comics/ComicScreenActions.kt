package me.emmano.state.comics

import me.emmano.state.AsyncStoreAction
import me.emmano.state.SyncStoreAction

object Loading : SyncStoreAction<ComicsViewModel.State> {
    override fun fire() = { state: ComicsViewModel.State -> state.copy(loading = true, showError = false) }
}

class LoadComics :
    AsyncStoreAction<ComicsViewModel.State> {

//    private val comicRepository by inject<ComicRepository>()

    override suspend fun fire(currentState: ComicsViewModel.State): (ComicsViewModel.State) -> ComicsViewModel.State {
//        val comics = comicRepository.comics().orEmpty()
        return {state: ComicsViewModel.State -> state.copy(listOf(ComicModel("HEY", "DESC", "")), false, false)}
    }
}

val ShowError = { state: ComicsViewModel.State -> state.copy(loading = false, showError = true) }