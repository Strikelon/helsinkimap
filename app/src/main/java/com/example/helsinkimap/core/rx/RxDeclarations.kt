package com.example.helsinkimap.core.rx

import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Predicate
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

/**
 * Useful with [Observable] & [com.jakewharton.rxrelay2.Relay] to avoid [io.reactivex.exceptions.MissingBackpressureException].
 *
 * @param strategy [BackpressureStrategy] Flowable convertion strategy.
 * @param tag used to store Disposable in [RxHelper].
 * @param helper [RxHelper] to store subscription.
 * @param scheduler [Observable.observeOn] scheduler. **Skipped if null**.
 * @param transformer [Flowable.compose] transformation. **Skipped if null**.
 * @param consumer [Flowable.subscribe] onNext consumer. last lambda outside arguments.
 */
fun <T> Observable<T>.observeSafe(
    scheduler: Scheduler? = null,
    tag: String? = null,
    helper: RxHelperUpdated? = null,
    strategy: BackpressureStrategy = BackpressureStrategy.LATEST,
    transformer: ((Flowable<T>) -> Publisher<T>)? = null,
    consumer: (T) -> Unit = {}
): Disposable = toFlowable(strategy)
    .run { takeIf { transformer != null }?.compose(transformer) ?: this }
    .run { takeIf { scheduler != null }?.observeOn(scheduler) ?: this }
    .subscribe(consumer)
    .apply { helper?.let { trackBy(tag, it) } }

fun Disposable.trackBy(rxHelper: RxHelperUpdated) =
    rxHelper.add(this)

fun Disposable.trackBy(tag: String?, rxHelper: RxHelperUpdated) =
    tag?.let { rxHelper.add(it, this) } ?: trackBy(rxHelper)

fun <T> Flowable<T>.debounceWithBuffer(
    debounceTime: Long,
    timeUnit: TimeUnit
): Flowable<MutableList<T>> =
    share().run { buffer(debounce(debounceTime, timeUnit)) }

fun <T> Observable<T>.debounceWithBuffer(debounceTime: Long, timeUnit: TimeUnit) =
    toFlowable(BackpressureStrategy.BUFFER).debounceWithBuffer(debounceTime, timeUnit)

fun <T : Any> Flowable<T>.indexed(): Flowable<Pair<T, Int>> {
    return zipWith(
        Flowable.range(0, Integer.MAX_VALUE),
        { item, index -> Pair(item, index) },
        false, 1
    )
}

fun <T> Completable.returnOriginalFlowable(original: T): Flowable<T> =
    this.andThen(Flowable.defer { Flowable.just(original) })

fun <T> Completable.returnOriginalSingle(original: T): Single<T> =
    this.andThen(Single.defer { Single.just(original) })

fun <T> Single<T>.filterOrError(predicate: Predicate<T>): Single<T> =
    this.filter(predicate).toSingle()
