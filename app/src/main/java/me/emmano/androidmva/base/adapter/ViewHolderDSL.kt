package me.emmano.androidmva.base.adapter

import androidx.databinding.ViewDataBinding

class ViewHolderDSL<B: ViewDataBinding, T : Identity<T>> {

    var bind: ((B, T)->Unit)? = null
    var onClick: ((T)->Unit)? = null

    fun onBind(bind: ((B, T)->Unit)?) {
        this.bind = bind
    }

    fun onClick(onClick: ((T)->Unit)?) {
        this.onClick = onClick
    }
}