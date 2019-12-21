package me.emmano.androidmva.comics.mvvm

import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.emmano.androidmva.base.*
import me.emmano.androidmva.comics.mvvm.ComicsViewModel.State
import me.emmano.androidmva.comics.repo.ComicRepository

class ComicsViewModel(private val comicRepository: ComicRepository, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : BaseViewModel2<State>(State(), Store()) {

    val comics = observe {
        it.comics
    }
    val loading = observe {
        it.loading
    }

    fun loadComics() {

        viewModelScope.launch(Dispatchers.IO) {
            action(Loading)

        }

            try {
                viewModelScope.launch(Dispatchers.IO) {
                    action(LoadComics(comicRepository))
                }

            } catch (e: LoadingComicsException) {
                viewModelScope.launch(Dispatchers.IO) {
                    action(ShowError)
                }

        }
    }

    data class State(
        val comics: List<ComicModel> = emptyList(),
        val loading: Boolean = false,
        val showError: Boolean = false
    )

}

