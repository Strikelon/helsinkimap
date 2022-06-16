package com.example.helsinkimap.data.coroutinescope

import kotlinx.coroutines.CoroutineScope

interface CoroutineScopeProvider {

    fun coroutineScope(): CoroutineScope
}
