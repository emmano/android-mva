package me.emmano.adapt

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import me.emmano.adapt.base.LooperMockRunner
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(LooperMockRunner::class)
class CustomAdapterTest {

    private val testObject by adapter<Model> {

        val TYPE_HEADER = 1
        val TYPE_CONTENT = 2

        onCreateViewHolder { parent, viewType ->
            when (viewType) {
                TYPE_HEADER -> holder<ViewDataBinding, Header>(parent, R.layout.header, BR.header)

                TYPE_CONTENT -> holder<ViewDataBinding, Content>(parent, R.layout.content, BR.content)

                else -> throw IllegalStateException("ViewHolder not supported for itemViewType: $viewType")
            }
        }

        getViewTypes {
            when (it) {
                is Header -> TYPE_HEADER
                is Content -> TYPE_CONTENT
            }
        }
    }

    @Test
    fun `onBindViewHolder - binds data for header`() = bindingTest { binding ->

        val header = mock<Header>()

        testObject.submitList(listOf(header))

        val holder = testObject.onCreateViewHolder(mock(), 1)

        testObject.onBindViewHolder(holder, 0)

        verify(binding).setVariable(BR.header, header)

        assertThat(testObject.itemCount, equalTo(1))
    }

}

fun bindingTest(block: (ViewDataBinding) -> Unit) {
    val context = mock<Context>()
    val inflater = LayoutInflater.from(context)

    val parent = mock<ViewGroup>()
    val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, 0, parent, false)
    whenever(binding.root) doReturn mock()
    block(binding)
}
