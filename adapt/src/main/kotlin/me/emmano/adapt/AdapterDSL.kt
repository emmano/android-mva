package me.emmano.adapt

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

class AdapterDSL<M : Identity<M>> {

    lateinit var create: (ViewGroup, Int) -> BindableViewHolder<*, M, *>
    lateinit var viewTypes: (M) -> Int

    fun onCreateViewHolder(create: (ViewGroup, Int) -> BindableViewHolder<*, M, *>) {
        this.create = create
    }

    fun getViewTypes(viewTypes: (M) -> Int) {
        this.viewTypes = viewTypes
    }

    fun <B : ViewDataBinding, T : Identity<T>> holder(
        parent: ViewGroup,
        layoutId: Int,
        modelId: Int,
        dsl: (ViewHolderDSL<B, T>.() -> Unit)? = null
    ) = BindableViewHolder.create<B, M, T>(
        parent,
        modelId,
        layoutId,
        ViewHolderDSL<B, T>().apply {
            dsl?.let { dsl(this) }
        })
}

fun <M : Identity<M>> adapter(dsl: AdapterDSL<M>.() -> Unit): Lazy<ModelAdapter<M>> =
    lazy { ModelAdapter(AdapterDSL<M>().apply { dsl() }) }

fun <M : Identity<M>> adapter(
    modelId: Int,
    layoutId: Int,
    onclick: ((M) -> Unit)? = null
): Lazy<ModelAdapter<M>> =
    lazy {
        ModelAdapter(
            AdapterDSL<M>()
            .apply {
                onCreateViewHolder { parent, _ ->
                    holder<ViewDataBinding, M>(parent, layoutId, modelId) {
                        onClick(onclick)
                    }
                }
                getViewTypes { 0 }
            })
    }

fun <M : Identity<M>, B : ViewDataBinding> adapter(
    modelId: Int,
    layoutId: Int,
    onBind: ((B, M) -> Unit)
): Lazy<ModelAdapter<M>> =
    lazy {
        ModelAdapter(
            AdapterDSL<M>()
            .apply {
                onCreateViewHolder { parent, _ ->
                    holder<B, M>(parent, layoutId, modelId) {
                        onBind(onBind)
                    }
                }

                getViewTypes { 0 }
            })
    }
