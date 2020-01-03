package me.emmano.androidmva.comics.mvvm

import me.emmano.androidmva.base.AsyncStoreAction
import me.emmano.androidmva.base.SyncStoreAction
import me.emmano.androidmva.comics.repo.ComicRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

object Loading : SyncStoreAction<ComicsViewModel.State> {
    override fun fire() = { state: ComicsViewModel.State -> state.copy(loading = true, showError = false) }
}

class LoadComics :
    AsyncStoreAction<ComicsViewModel.State>, KoinComponent {

    private val comicRepository by inject<ComicRepository>()

    override suspend fun fire(currentState: ComicsViewModel.State): (ComicsViewModel.State) -> ComicsViewModel.State {
        val comics = comicRepository.comics().orEmpty()
        return {state: ComicsViewModel.State -> state.copy(comics, false, false)}
    }
}

val ShowError = { state: ComicsViewModel.State -> state.copy(loading = false, showError = true) }