package me.emmano.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

actual open class ScopeProvider: ViewModel(){
    actual val scope: CoroutineScope = viewModelScope

    actual override fun onCleared() {

    }
}