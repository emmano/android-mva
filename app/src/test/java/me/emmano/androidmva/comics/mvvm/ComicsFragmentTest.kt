package me.emmano.androidmva.comics.mvvm

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.emmano.androidmva.CoroutineTest
import me.emmano.androidmva.R
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
    private val comics = MutableLiveData<List<ComicModel>>()

    @Before
    fun setUp() {
        declareViewModelMock<ComicsViewModel> { on { comics } doReturn comics }
    }

    @Test
    fun `onViewCreated loads and displays comics`() = test {
        val comicCell = ComicCell("title", "description", "imageUrl")
        launchFragmentInContainer<ComicsFragment>()

        verify(viewModel).loadComics()

        comics.value = listOf(comicCell)

        onView(withId(R.id.comics))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                hasDescendant(withText(comicCell.title)),
                click()))

    }
}