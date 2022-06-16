package com.example.helsinkimap.data.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {

    fun ui(): CoroutineDispatcher

    fun computation(): CoroutineDispatcher

    fun io(): CoroutineDispatcher
}
