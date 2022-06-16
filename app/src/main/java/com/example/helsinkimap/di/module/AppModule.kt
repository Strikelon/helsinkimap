package com.example.helsinkimap.di.module

import com.example.helsinkimap.data.coroutinescope.AppCoroutineScope
import com.example.helsinkimap.data.coroutinescope.CoroutineScopeProvider
import com.example.helsinkimap.data.dispatcher.AppDispatchers
import com.example.helsinkimap.data.dispatcher.DispatchersProvider
import com.example.helsinkimap.data.location.datasource.LocationLocalDataSource
import com.example.helsinkimap.data.network.datasource.ActivitiesRemoteDatasource
import com.example.helsinkimap.data.repository.CityEntertainmentRepository
import com.example.helsinkimap.data.repository.LocationRepository
import com.example.helsinkimap.specs.api.repositories.CityEntertainmentRepositoryApi
import com.example.helsinkimap.specs.api.repositories.LocationRepositoryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideCoroutineScopes(): CoroutineScopeProvider = AppCoroutineScope()

    @Provides
    @Singleton
    fun provideDispatchers(): DispatchersProvider = AppDispatchers()

    @Provides
    @Singleton
    fun provideMapRepository(
        activitiesRemoteDatasource: ActivitiesRemoteDatasource
    ): CityEntertainmentRepositoryApi =
        CityEntertainmentRepository(
            activitiesRemoteDatasource
        )

    @Provides
    @Singleton
    fun provideLocationRepository(
        locationLocalDataSource: LocationLocalDataSource
    ): LocationRepositoryApi =
        LocationRepository(
            locationLocalDataSource
        )
}
