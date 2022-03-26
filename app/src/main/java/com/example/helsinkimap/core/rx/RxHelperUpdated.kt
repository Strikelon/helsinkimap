package com.example.helsinkimap.core.rx

import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference
import java.util.WeakHashMap

private typealias ReferenceList<T> = MutableList<WeakReference<T>>
private typealias ReferenceMap<T> = MutableMap<String, ReferenceList<T>>
private typealias ReferenceMapEntry<T> = Map.Entry<String, ReferenceList<T>>

@Suppress("unused", "MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate")
class RxHelperUpdated {

    companion object {
        const val TAG = "\$\$RxHelper"
    }

    private val tagged: ReferenceMap<Disposable> = WeakHashMap()

    /**
     * Add new [Disposable].
     *
     * @param disposable to keep eye on.
     * @return self arg.
     */
    fun add(disposable: Disposable): Disposable {
        add(TAG, disposable)
        return disposable
    }

    fun addSingle(disposable: Disposable, tag: String): Disposable {
        tagged.remove(tag)?.disposeAll()
        add(tag, disposable)
        return disposable
    }

    fun addAll(vararg disposables: Disposable) {
        disposables.forEach { add(it) }
    }

    /**
     * Add new Disposable to collection associated with tag.
     *
     * @param tag        associate with collection under this tag
     * @param disposable to keep eye on.
     * @return self [disposable] arg
     */
    fun add(tag: String, disposable: Disposable): Disposable {
        disposable.also {
            getDisposables(tag).add(WeakReference(it))
        }
        return disposable
    }

    fun addAll(tag: String, vararg disposables: Disposable) {
        disposables.forEach { add(tag, it) }
    }

    /**
     * Stop single disposable.
     */
    fun unsubscribe(disposable: Disposable) = disposable.also {
        tagged.asIterable().find { references -> references.removeReference(it) }
        dispose(it)
    }

    /**
     * Unsubscribe every disposable associated with tag.
     *
     * @param tag associated with collection of disposables
     */
    fun unsubscribeBy(tag: String) {
        tagged.remove(tag)?.disposeAll()
    }

    fun unsubscribeAll() = with(tagged.iterator()) {
        while (hasNext()) {
            next().value.disposeAll()
            remove()
        }
    }

    fun inProgress(disposable: Disposable?): Boolean {
        return disposable?.isDisposed?.not() ?: false
    }

    fun anyInProgress(tag: String): Boolean {
        return getDisposables(tag).any { inProgress(it.get()) }
    }

    private fun getDisposables(tag: String): ReferenceList<Disposable> {
        return tagged.getOrPut(tag) { mutableListOf() }
    }

    private fun <T> ReferenceMapEntry<T>.removeReference(disposable: Disposable): Boolean {
        return value.remove(findReference(disposable))
    }

    private fun <T> ReferenceMapEntry<T>.findReference(disposable: Disposable): WeakReference<T>? {
        return value.find { disposable == it.get() }
    }

    private fun ReferenceList<Disposable>.disposeAll() {
        forEach { dispose(it.get()) }
    }

    private fun dispose(disposable: Disposable?) {
        disposable?.takeUnless { it.isDisposed }?.dispose()
    }
}
