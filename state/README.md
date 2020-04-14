# What is State?
State is a state management framework for the View layer. It is inspired by redux, but only applied for the `ViewModel`->`View` portion of an app.

# Usage

State provides a `BaseViewModel` class that your `ViewModel` should extend. It also provides a `ViewStateProvider` class that is reponsible to managing the state.

```kotlin
package me.emmano.androidmva.comics.mvvm

import me.emmano.androidmva.comics.mvvm.ComicsViewModel.State
import me.emmano.state.BaseViewModel
import me.emmano.state.ViewStateProvider

class ComicsViewModel(viewStateProvider: ViewStateProvider<State>) :
    BaseViewModel<State>(viewStateProvider) {

    override val errors = { t: Throwable ->
        ShowError
    }

    val comics by observe {
        it.comics
    }
    val loading by observe {
        it.loading
    }

    fun loadComics() {
        action(Loading)
        action(LoadComics())
    }

    data class State(
        val comics: List<ComicModel> = emptyList(),
        val loading: Boolean = false,
        val showError: Boolean = false
    )

}
```

`observe` is an extension function provided by `BaseViewModel` that should be used to map the underlying state, `State` in this case, to the actual data stream to be observed. `observe` returns a `LiveData` that will only emit if the previous value is different than the current.

`action()` takes in `StoreActions`(to be renamed) that currently can be one of two types, a `SyncStoreAction` or an `AsyncStoreAction`.

`SyncStoreAction` are meant to be used for changes in state that are immediate.
`AsyncStoreAction` are meant to be used for changes in state that are asynchronous.

Here are examples for both both:

```kotlin
package me.emmano.androidmva.comics.mvvm

import me.emmano.androidmva.comics.repo.ComicRepository
import me.emmano.state.AsyncStoreAction
import me.emmano.state.SyncStoreAction
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
```
One cool feature of State is that `SyncActions` will be executed even there is an ongoing `AsyncAction`. The framework will make sure that `AsyncActions` always get the latest available state.

In this example, we use Koin to inject the a dependency into the `AsyncAction`.

For a complete example look [here](https://github.com/emmano/android-mva/blob/master/app/src/main/java/me/emmano/androidmva/comics/mvvm/ComicScreenActions.kt).

To use state, just add the dependency to your app's module `build.gradle`.

`implementation me.emmano:state:x.y.z`

TODO:

1. Add better support for error handling.

**Note**: This library is a port of [@ephraimschmitt](https://github.com/ephraimschmitt) model-stream-event state management framework for Dart that.
