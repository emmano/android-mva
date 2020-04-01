package me.emmano.adapt

import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import me.emmano.adapt.base.LooperMockRunner
import me.emmano.adapt.base.Patch
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(LooperMockRunner::class)
class ModelAdapterTest  {

    @Test
    fun `onCreateViewHolder - delegates to dsl for view holder creation`() {
        android.osx.Looper()
        Looper.getMainLooper()
//        android.viewx.LayoutInflater.from(mock())

        val bindableViewHolder : BindableViewHolder<*, Model, *> = BindableViewHolder<ViewDataBinding, Model, Model>(
            mock{ on { root } doReturn mock()}, 0, mock()
        )
        val viewHolder : (ViewGroup, Int) -> BindableViewHolder<*, Model, *> =  { _, _ ->
            bindableViewHolder
        }
        val adapterDSL = AdapterDSL<Model>().apply { onCreateViewHolder(viewHolder) }
        val testObject = ModelAdapter(adapterDSL)

        val viewType = 0
        val parent = mock<ViewGroup>()

        val holder = testObject.onCreateViewHolder(parent, viewType)

        assertThat(holder, equalTo(bindableViewHolder))
    }

    @Test
    fun `getItemViewType - delegates to dsl for view types`() {

        Looper.getMainLooper()
        val model = mock<Model>()
        val viewType = 1
        val viewTypes: (Model) -> Int = {viewType}
        val adapterDSL = AdapterDSL<Model>().apply { getViewTypes(viewTypes) }

        val testObject = ModelAdapter(adapterDSL)
        testObject.submitList(listOf(model))

        val position = 0

        val actualViewType = testObject.getItemViewType(position)

        assertThat(actualViewType, equalTo(viewType))

    }

    @Test
    fun `onBindViewHolder - delegates to dsl for view types`() {
        Looper.getMainLooper()

        val model = mock<Model>()
        val adapterDSL: AdapterDSL<Model> = mock()

        val testObject = ModelAdapter(adapterDSL)

        testObject.submitList(listOf(model))

        val position = 0

        val holder = mock<BindableViewHolder<*, Model, *>>()

        testObject.onBindViewHolder(holder, position)

        verify(holder) bind model
    }
}