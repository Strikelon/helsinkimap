package com.example.helsinkimap.presentation.arch.delegate

import android.os.Bundle
import android.view.View

/**
 * Helper LifecycleDelegate that stores current lifecycle state
 */
class StateLifecycleDelegate : LifecycleDelegate {

    var isCreated = false
        private set

    var isViewCreated = false
        private set

    var isStarted = false
        private set

    var isResumed = false
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        isCreated = true
    }

    override fun onCreateView(rootView: View) {
        isViewCreated = true
    }

    override fun onStart() {
        isStarted = true
    }

    override fun onResume() {
        isResumed = true
    }

    override fun onPause() {
        isResumed = false
    }

    override fun onStop() {
        isStarted = false
    }

    override fun onDestroyView() {
        isViewCreated = false
    }

    override fun onDestroy() {
        isCreated = false
    }
}
