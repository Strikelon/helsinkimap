package com.example.helsinkimap.data.coroutinescope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppCoroutineScope @Inject constructor() : CoroutineScopeProvider {

    override fun coroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob())
}
