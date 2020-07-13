package me.emmano.androidmva.comics.mvvm

import me.emmano.androidmva.comics.repo.ComicRepository
import me.emmano.state.AsyncStoreAction
import me.emmano.state.SyncStoreAction
import javax.inject.Inject

object Loading : SyncStoreAction<ComicsViewModel.State> {
    override fun fire() = { state: ComicsViewModel.State -> state.copy(loading = true, showError = false) }
}
class LoadComics @Inject constructor(private val comicRepository: ComicRepository) :
    AsyncStoreAction<ComicsViewModel.State> {

    override suspend fun fire(currentState: ComicsViewModel.State): (ComicsViewModel.State) -> ComicsViewModel.State {

        val comics = comicRepository.comics().orEmpty()
        return {state: ComicsViewModel.State -> state.copy(comics, false, false)}
    }
}

val ShowError = { state: ComicsViewModel.State -> state.copy(loading = false, showError = true) }