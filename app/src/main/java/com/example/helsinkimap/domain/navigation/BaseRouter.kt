package com.example.helsinkimap.domain.navigation

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Router

@Suppress("unused")
open class BaseRouter(private val cicerone: Cicerone<Router>) {

    @Suppress("MemberVisibilityCanBePrivate")
    var cache: PagesCache? = null

    val router: Router
        get() = cicerone.router

    fun exit() = router.exit()

    fun setNavigator(navigator: Navigator) = cicerone.navigatorHolder.setNavigator(navigator)

    fun removeNavigator() = cicerone.navigatorHolder.removeNavigator()

    // Access from different threads (from interactors)
    fun clearCache() = synchronized(this) {
        cache?.clearCache().also { cache = null }
    }

    /**
     * Get stored cache if one was stored. Create with creator function otherwise.
     *
     * @param cacheCreator fabric method for cache creation.
     * @param <T>          MainRouterCache implementation.
     * @return previous or newly created cache object.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : PagesCache> getOrCreateCache(cacheCreator: () -> T) = synchronized(this) {
        when (cache) {
            null -> cacheCreator().also { cache = it }
            else -> cache as T
        }
    }

    interface PagesCache {
        fun clearCache()
    }
}
