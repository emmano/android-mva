package me.emmano.androidmva.comics.mvvm

import me.emmano.androidmva.base.BaseViewModel2

class ComicsViewModel(comicsStore: ComicsStore)
    : BaseViewModel2<ComicsViewModel.State, StoreAction>(State(), comicsStore) {

    val comics by observe {
        it.comics
    }
    val loading by observe {
        it.loading
    }

     fun loadComics() {
        action(Loading)
            try {
                action(Loading)
                action(LoadComics)
            } catch (e: LoadingComicsException) {
                action(LoadingError)
            }
    }

    data class State(
        val comics: List<ComicModel> = emptyList(),
        val loading: Boolean = false,
        val showError: Boolean = false
    )


}

