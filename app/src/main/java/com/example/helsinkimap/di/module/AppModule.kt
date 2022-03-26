package com.example.helsinkimap.di.module

import android.content.Context
import com.example.helsinkimap.HelsinkiApplication
import com.example.helsinkimap.domain.navigation.MainRouter
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Cicerone
import javax.inject.Singleton

@Module
class AppModule(
    /**
     * Application instance.
     */
    private val application: HelsinkiApplication
) {

    /**
     * Provides application instance injecting.
     */
    @Provides
    @Singleton
    fun provideApplication(): HelsinkiApplication = application

    /**
     * Provides context instance injecting.
     */
    @Provides
    @Singleton
    fun provideContext(): Context = application

    @Provides
    @Singleton
    /**
     * Provides MainRouter for navigation through fragments of MainActivity
     */
    fun provideCiceroni(): MainRouter = MainRouter(Cicerone.create())
}
