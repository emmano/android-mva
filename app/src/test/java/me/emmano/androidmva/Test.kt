package me.emmano.androidmva

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.scan
import me.emmano.androidmva.base.LoadComics
import me.emmano.androidmva.base.Loading
import me.emmano.androidmva.base.AsyncStoreAction
import me.emmano.androidmva.comics.mvvm.ComicsViewModel
import me.emmano.androidmva.comics.repo.ComicRepository
import me.emmano.androidmva.rule.CoroutineTestRule
import org.junit.Rule
import org.junit.Test
import timber.log.Timber

class Test : CoroutineTest{

    override val coroutineRule: CoroutineTestRule = CoroutineTestRule()


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @InternalCoroutinesApi
    @Test
    fun name() = runBlocking<Unit> {
println()
        val actions by lazy { ConflatedBroadcastChannel<String>()}

//        launch {
//            actions.consumeEach {
//                assertEquals("hello", it)
//                assertEquals("world", it)
//            }
//
//        }

launch {actions.offer("hello")  }

            launch {
                actions.offer("world")

            }


        val flow = actions.asFlow()
        flow.asLiveData().observeForever {
                it
            }

        flow.scan("x"){ value, acc ->value + acc }.asLiveData().observeForever {
            it

        }



    }

    @Test
    fun test() = runBlocking {
        val testObject = Store<ComicsViewModel.State>(ComicsViewModel.State())
        val repository: ComicRepository = mock { onBlocking { comics() } doReturn mock() }

        testObject.dispatch(Loading)
        testObject.dispatch(LoadComics(repository))


    }
}

class Store<S>(initialState: S) {

    val actions by lazy { BroadcastChannel<AsyncStoreAction<S>>(1) }

    suspend fun dispatch(action: AsyncStoreAction<S>) {
        actions.send(action)
    }

    @VisibleForTesting
    val stateLiveDatax: LiveData<S> by lazy {

            actions
            .asFlow()
            .scan(initialState)
            { value, acc ->
                Timber.e("Thread ${Thread.currentThread().name} acc: $acc value: $value")
                acc.reduce(value)
            }
            .asLiveData()

    }

}