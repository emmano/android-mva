package me.emmano.androidmva.comics.mvvm

import me.emmano.androidmva.base.BaseViewModel
import me.emmano.androidmva.comics.mvvm.ComicsViewModel.State
import me.emmano.androidmva.comics.repo.ComicRepository

class ComicsViewModel(private val comicRepository: ComicRepository) : BaseViewModel<State>(State()) {

    val comics by lazy {  observe { it.comics } }
    val loading = observe { it.loading }

    fun loadComics() {

        updateState { it.copy(loading = true, showError = false) }

        comicRepository.comics.subscribe(
            { comics -> updateState { it.copy(comics = comics.orEmpty(), loading = false, showError = false) } },
            { updateState { it.copy(loading = false, showError = true) } }
        ).autoDispose()
    }

    data class State(
        val comics: List<ComicModel> = emptyList(),
        val loading: Boolean = false,
        val showError: Boolean = false
    )

}

