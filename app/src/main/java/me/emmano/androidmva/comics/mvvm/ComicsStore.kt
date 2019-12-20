package me.emmano.androidmva.comics.mvvm

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import me.emmano.androidmva.comics.repo.ComicRepository
import java.lang.IllegalStateException
import java.util.concurrent.Executors

class ComicsStore(
    private val comicsRepository: ComicRepository,
    override val dispatcher: CoroutineDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
) : Store<ComicsViewModel.State, StoreAction> {

    override suspend fun reduce(action: StoreAction, state: ComicsViewModel.State): ComicsViewModel.State =
        when (action) {
            is Loading -> state.copy(loading = true, showError = false)
            is LoadComics -> {
                val comics = comicsRepository.comics().orEmpty()
                state.copy(comics = comics, loading = false, showError = false)
            }
            is LoadingError -> state.copy(comics = emptyList(), loading = false, showError = true)
            else -> throw IllegalStateException()
        }
}

object Loading : StoreAction
object LoadComics : StoreAction
object LoadingError : StoreAction