package com.example.helsinkimap.specs.api.providers

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {

    fun ui(): CoroutineDispatcher

    fun computation(): CoroutineDispatcher

    fun io(): CoroutineDispatcher
}
