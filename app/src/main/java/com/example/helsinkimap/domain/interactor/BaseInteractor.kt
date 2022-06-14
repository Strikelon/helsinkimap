package com.example.helsinkimap.domain.interactor

import com.example.helsinkimap.data.scheduler.SchedulersProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

open class BaseInteractor(
    val schedulers: SchedulersProvider
) {
    protected fun <T> Observable<T>.schedule(): Observable<T> =
        this.compose(schedulers.applyToObservable())

    protected fun <T> Flowable<T>.schedule(): Flowable<T> =
        this.compose(schedulers.applyToFlowable())

    protected fun <T> Single<T>.schedule(): Single<T> = this.compose(schedulers.applyToSingle())

    protected fun <T> Maybe<T>.schedule(): Maybe<T> = this.compose(schedulers.applyToMaybe())

    protected fun Completable.schedule(): Completable =
        this.compose(schedulers.applyToCompletable())
}
