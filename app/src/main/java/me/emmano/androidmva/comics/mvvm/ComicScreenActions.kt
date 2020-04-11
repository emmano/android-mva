package me.emmano.androidmva.comics.mvvm

import me.emmano.state.SyncStoreAction

object Loading : SyncStoreAction<ComicsViewModel.State> {
    override fun fire() =
        { state: ComicsViewModel.State -> state.copy(loading = true, showError = false) }
}

class LoadComics(private val comics: List<ComicModel>) :
    SyncStoreAction<ComicsViewModel.State> {

    override fun fire() = { state: ComicsViewModel.State ->
        state.copy(
            loading = false,
            comics = comics,
            showError = false
        )
    }
}
object Error : SyncStoreAction<ComicsViewModel.State> {
    override fun fire() =
        { state: ComicsViewModel.State -> state.copy(loading = true, showError = true) }
}

val ShowError = { state: ComicsViewModel.State -> state.copy(loading = false, showError = true) }