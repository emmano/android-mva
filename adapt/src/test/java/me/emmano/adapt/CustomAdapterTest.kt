package me.emmano.adapt

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import me.emmano.adapt.base.LooperMockRunner
import me.emmano.adapt.base.Patch
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(LooperMockRunner::class)
@Patch(AsyncListDiffer::class)
class CustomAdapterTest {

    @Test
    fun name() {
        val inflater =
            mock<LayoutInflater> {
                on { inflate(any<Int>(), any(), any()) } doReturn mock()
            }

        val context =
            mock<Context> { on { getSystemService(Context.LAYOUT_INFLATER_SERVICE) } doReturn inflater }


        val parent = mock<ViewGroup> {

            on { this.context } doReturn context
        }

        val testObject = CustomAdapter()


        val holder = testObject.onCreateViewHolder(parent, 0)

    }
}