package me.emmano.adapt

import androidx.databinding.ViewDataBinding

class ViewHolderDSL<B: ViewDataBinding, T : Identity<T>> {

    internal var bind: ((B, T)->Unit)? = null
    internal var onClick: ((T)->Unit)? = null

    fun onBind(bind: ((B, T)->Unit)?) {
        this.bind = bind
    }

    fun onClick(onClick: ((T)->Unit)?) {
        this.onClick = onClick
    }
}