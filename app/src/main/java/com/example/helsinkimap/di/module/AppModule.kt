package com.example.helsinkimap.di.module

import android.content.Context
import com.example.helsinkimap.HelsinkiApplication
import dagger.Module
import dagger.Provides
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
    fun provideApplication(): HelsinkiApplication {
        return application
    }

    /**
     * Provides context instance injecting.
     */
    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }
}
