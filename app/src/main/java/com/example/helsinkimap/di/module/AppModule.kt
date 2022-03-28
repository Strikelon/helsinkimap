package com.example.helsinkimap.di.module

import android.content.Context
import com.example.helsinkimap.HelsinkiApplication
import com.example.helsinkimap.data.location.datasource.LocationDataSource
import com.example.helsinkimap.data.network.datasource.NetworkApiDatasource
import com.example.helsinkimap.data.repository.CityEntertainmentRepository
import com.example.helsinkimap.data.repository.LocationRepository
import com.example.helsinkimap.data.scheduler.AppSchedulers
import com.example.helsinkimap.data.scheduler.SchedulersProvider
import com.example.helsinkimap.domain.interactor.MapInteractor
import com.example.helsinkimap.domain.navigation.MainRouter
import com.example.helsinkimap.specs.api.interactors.MapInteractorApi
import com.example.helsinkimap.specs.api.repositories.CityEntertainmentRepositoryApi
import com.example.helsinkimap.specs.api.repositories.LocationRepositoryApi
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

    /**
     * Provides MainRouter for navigation through fragments of MainActivity
     */
    @Provides
    @Singleton
    fun provideCiceroni(): MainRouter = MainRouter(Cicerone.create())

    /**
     * Provides Schedulers for switching threads in interactors
     */
    @Provides
    @Singleton
    fun provideSchedulers(): SchedulersProvider = AppSchedulers()

    // Repositories

    @Provides
    @Singleton
    fun provideMapRepository(
        networkApiDatasource: NetworkApiDatasource
    ): CityEntertainmentRepositoryApi =
        CityEntertainmentRepository(
            networkApiDatasource
        )

    @Provides
    @Singleton
    fun provideLocationRepository(
        locationDataSource: LocationDataSource
    ): LocationRepositoryApi =
        LocationRepository(
            locationDataSource
        )

    // Interactors

    @Provides
    fun provideMapInteractor(
        cityEntertainmentRepository: CityEntertainmentRepositoryApi,
        locationRepository: LocationRepositoryApi,
        schedulers: SchedulersProvider
    ): MapInteractorApi =
        MapInteractor(
            cityEntertainmentRepository,
            locationRepository,
            schedulers
        )
}
