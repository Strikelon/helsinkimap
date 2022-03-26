package com.example.helsinkimap.presentation.arch.delegate

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope

/**
 * A helper interface that allows to define callbacks for lifecycle events. Classes with lifecycle that support this,
 * should call methods of all delegates when lifecycle event occurs.
 *
 * It is more simple, stable and consistent than [androidx.lifecycle.LifecycleObserver]
 */
interface LifecycleDelegate {

    fun setLifecycleScopeProvider(scope: () -> LifecycleCoroutineScope) {}

    fun onCreate(savedInstanceState: Bundle?) {}

    // Called *after* view is created unlike default Fragment behavior.
    // Naming is like that for consistency with onDestroyView
    fun onCreateView(rootView: View) {}

    fun onStart() {}

    fun onResume() {}

    fun onPause() {}

    fun onStop() {}

    fun onDestroyView() {}

    fun onSaveInstanceState(outState: Bundle) {}

    fun onDestroy() {}
}
