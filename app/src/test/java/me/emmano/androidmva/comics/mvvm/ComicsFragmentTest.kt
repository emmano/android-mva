package me.emmano.androidmva.comics.mvvm

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.emmano.androidmva.CoroutineTest
import me.emmano.androidmva.base.RobolectricTest
import me.emmano.androidmva.rule.CoroutineTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.inject

@ExperimentalCoroutinesApi
class ComicsFragmentTest : RobolectricTest(), CoroutineTest {

    @get:Rule
    override val coroutineRule: CoroutineTestRule = CoroutineTestRule()

    private val viewModel: ComicsViewModel by inject()

    @Before
    fun setUp() {
        declareViewModelMock<ComicsViewModel> { on { comics } doReturn MutableLiveData() }

        launchFragmentInContainer<ComicsFragment>()
    }

    @Test
    fun `onViewCreated loads comics`() = test {
        verify(viewModel).loadComics()
    }
}