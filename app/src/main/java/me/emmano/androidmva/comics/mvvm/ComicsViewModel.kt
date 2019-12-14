package me.emmano.androidmva.comics.mvvm

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.emmano.androidmva.base.BaseViewModel
import me.emmano.androidmva.comics.mvvm.ComicsViewModel.State
import me.emmano.androidmva.comics.repo.ComicRepository

class ComicsViewModel(private val comicRepository: ComicRepository, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : BaseViewModel<State>(State()) {

    val comics by observe { it.comics }
    val loading by observe { it.loading }

    suspend fun loadComics() {
        updateState { it.copy(loading = true, showError = false) }
        viewModelScope.launch(dispatcher) {
            try {
                val comics = comicRepository.comics()
                updateState { it.copy(comics = comics.orEmpty(), loading = false, showError = false) }
            } catch (e: LoadingComicsException) {
                updateState { it.copy(loading = false, showError = true) }
            }
        }
    }

    data class State(
        val comics: List<ComicModel> = emptyList(),
        val loading: Boolean = false,
        val showError: Boolean = false
    )

}

