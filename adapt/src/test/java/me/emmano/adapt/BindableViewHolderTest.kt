package me.emmano.adapt

import android.view.View
import androidx.databinding.ViewDataBinding
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import me.emmano.adapt.base.LooperMockRunner
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith

class BindableViewHolderTest {

    @Test
    fun `bind - set variable on binding`() {
        val rootView: View = mock()
        val binding = mock<ViewDataBinding> {on { root } doReturn rootView }
        val modelVariableId = 1
        val testObject = BindableViewHolder<ViewDataBinding, Model, Model>(binding, modelVariableId, null)
        val model: Content = mock()

        testObject bind model

        verify(binding).setVariable(modelVariableId, model)

    }

    @Test
    fun `bind - execute pending bindings`() {
        val rootView: View = mock()
        val binding = mock<ViewDataBinding> {on { root } doReturn rootView }
        val modelVariableId = 1
        val testObject = BindableViewHolder<ViewDataBinding, Model, Model>(binding, modelVariableId, null)
        val model: Content = mock()

        testObject bind model

        verify(binding).executePendingBindings()

    }

    @Test
    fun `bind - dsl onClick sets listener on rootView`() {
        val rootView: View = mock()
        val binding = mock<ViewDataBinding> {on { root } doReturn rootView }
        val clickAction: (Model)->Unit = mock()
        val dsl = mock<ViewHolderDSL<ViewDataBinding, Model>>{on { onClick } doReturn clickAction }
        val testObject = BindableViewHolder<ViewDataBinding, Model, Model>(binding, 1, dsl)
        val model: Model = mock()

        testObject bind model

        val captor = argumentCaptor<View.OnClickListener>()
        verify(rootView).setOnClickListener(captor.capture())

        captor.firstValue.onClick(rootView)

        verify(clickAction).invoke(model)
    }

    @Test
    fun `bind - dsl bind delegates binding`() {
        val rootView: View = mock()
        val binding = mock<ViewDataBinding> {on { root } doReturn rootView }
        val bindingAction: (ViewDataBinding, Model)->Unit = mock()
        val dsl = mock<ViewHolderDSL<ViewDataBinding, Model>>{on { bind } doReturn bindingAction }
        val testObject = BindableViewHolder<ViewDataBinding, Model, Model>(binding, 1, dsl)
        val model: Model = mock()

        testObject bind model

        verify(bindingAction).invoke(binding, model)
    }

    @Test
    fun `bind - dsl bind casts to specific model type`() {
        val rootView: View = mock()
        val binding = mock<ViewDataBinding> {on { root } doReturn rootView }
        val bindingAction: (ViewDataBinding, Content)->Unit = mock()
        val dsl = mock<ViewHolderDSL<ViewDataBinding, Content>>{on { bind } doReturn bindingAction }
        val testObject = BindableViewHolder<ViewDataBinding, Model, Content>(binding, 1, dsl)
        val model: Content = mock()

        testObject bind model as Model

        verify(bindingAction).invoke(binding, model)
    }

    @Test
    fun `bind - dsl onClick casts to specific model type`() {
        val rootView: View = mock()
        val binding = mock<ViewDataBinding> {on { root } doReturn rootView }
        val clickAction: (Content)->Unit = mock()
        val dsl = mock<ViewHolderDSL<ViewDataBinding, Content>>{on { onClick } doReturn clickAction }
        val testObject = BindableViewHolder<ViewDataBinding, Model, Content>(binding, 1, dsl)
        val model: Content = mock()

        testObject bind model

        val captor = argumentCaptor<View.OnClickListener>()
        verify(rootView).setOnClickListener(captor.capture())

        captor.firstValue.onClick(rootView)

        verify(clickAction).invoke(model)
    }

}


