package com.example.helsinkimap.presentation.arch.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helsinkimap.core.rx.RxHelperUpdated
import com.example.helsinkimap.core.rx.trackBy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

@Suppress("MemberVisibilityCanBePrivate")
abstract class MvvmViewModel : ViewModel() {

    val rxHelper by lazy { RxHelperUpdated() }

    val loadingProgressState: LiveData<Boolean> by lazy { MutableLiveData() }
    val loadingErrorEvent: LiveData<String?> by lazy { SingleLiveData() }

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

    protected fun <T> LiveData<T>.postValue(value: T?) {
        (this as? MutableLiveData<T>)?.postValue(value)
    }

    protected fun <T> LiveData<T>.setValue(value: T?) {
        (this as? MutableLiveData<T>)?.value = value
    }

    /**
     * Shows progress spinner until first data is loaded (from cache).
     */
    protected fun <T> Observable<T>.withProgressFromCache(): Observable<T> {
        var notHidden = true
        val hideIfNotHidden = {
            if (notHidden) {
                loadingProgressState.postValue(false)
                notHidden = false
            }
        }
        return this
            .doOnSubscribe { loadingProgressState.postValue(true) }
            .doOnEach { hideIfNotHidden() }
            .doFinally { hideIfNotHidden() }
    }

    protected fun <T> Flowable<T>.withProgressFromCache(): Flowable<T> {
        var notHidden = true
        val hideIfNotHidden = {
            if (notHidden) {
                loadingProgressState.postValue(false)
                notHidden = false
            }
        }
        return this
            .doOnSubscribe { loadingProgressState.postValue(true) }
            .doOnEach { hideIfNotHidden() }
            .doFinally { hideIfNotHidden() }
    }

    /**
     * Shows progress spinner until all data is loaded (from server).
     */
    protected fun <T> Flowable<T>.withProgress(): Flowable<T> = this
        .doOnSubscribe { loadingProgressState.postValue(true) }
        .doFinally { loadingProgressState.postValue(false) }

    protected fun <T> Observable<T>.withProgress(): Observable<T> = this
        .doOnSubscribe { loadingProgressState.postValue(true) }
        .doFinally { loadingProgressState.postValue(false) }

    protected fun <T> Single<T>.withProgress(): Single<T> = this
        .doOnSubscribe { loadingProgressState.postValue(true) }
        .doFinally { loadingProgressState.postValue(false) }

    /**
     * Shows progress spinner for special occasions when there is another predicate with
     * .withProgress() inside onSuccess block of subscribe(). In that case onFinally()
     * calls after onSubscribe() removing the progress spinner
     */
    protected fun <T> Single<T>.withProgressAlternative(): Single<T> = this
        .doOnSubscribe { loadingProgressState.postValue(true) }
        .doOnSuccess { loadingProgressState.postValue(false) }
        .doOnError { loadingProgressState.postValue(false) }

    protected fun <T> Maybe<T>.withProgress(): Maybe<T> = this
        .doOnSubscribe { loadingProgressState.postValue(true) }
        .doFinally { loadingProgressState.postValue(false) }

    protected fun Completable.withProgress(): Completable = this
        .doOnSubscribe { loadingProgressState.postValue(true) }
        .doFinally { loadingProgressState.postValue(false) }

    /**
     * Shows progress spinner non-removable (from server).
     */

    protected fun <T> Flowable<T>.withProgressNonRemovable(): Flowable<T> = this
        .doOnSubscribe { loadingProgressState.postValue(true) }

    protected fun <T> Observable<T>.withProgressNonRemovable(): Observable<T> = this
        .doOnSubscribe { loadingProgressState.postValue(true) }

    protected fun <T> Single<T>.withProgressNonRemovable(): Single<T> = this
        .doOnSubscribe { loadingProgressState.postValue(true) }

    protected fun <T> Maybe<T>.withProgressNonRemovable(): Maybe<T> = this
        .doOnSubscribe { loadingProgressState.postValue(true) }

    protected fun Completable.withProgressNonRemovable(): Completable = this
        .doOnSubscribe { loadingProgressState.postValue(true) }

    protected fun <T> Observable<T>.subscribeWithErrorLogging(
        onNext: (T) -> Unit,
        onError: ((Throwable) -> Unit)? = null
    ): Disposable {
        return this.subscribe(
            onNext,
            {
                onError?.invoke(it)
            }
        )
    }
}
