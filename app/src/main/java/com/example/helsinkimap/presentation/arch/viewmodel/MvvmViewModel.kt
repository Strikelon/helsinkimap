package com.example.helsinkimap.presentation.arch.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.example.helsinkimap.core.rx.RxHelperUpdated
import com.example.helsinkimap.core.rx.trackBy
import io.reactivex.disposables.Disposable

@Suppress("MemberVisibilityCanBePrivate")
abstract class MvvmViewModel : ViewModel() {

    val rxHelper by lazy { RxHelperUpdated() }

    open fun attach() {
    }

    open fun detach() {
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        rxHelper.unsubscribeAll()
    }

    protected fun unsubscribeOnDestroy(vararg disposables: Disposable) {
        for (disposable in disposables) {
            rxHelper.add(disposable)
        }
    }

    protected fun Disposable.unsubscribeOnDestroy() = trackBy(rxHelper)
}
