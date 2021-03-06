package com.example.helsinkimap.di.module

import com.example.helsinkimap.data.dispatcher.AppDispatchers
import com.example.helsinkimap.data.location.datasource.LocationLocalDataSource
import com.example.helsinkimap.data.network.datasource.ActivitiesRemoteDatasource
import com.example.helsinkimap.data.repository.CityEntertainmentRepository
import com.example.helsinkimap.data.repository.LocationRepository
import com.example.helsinkimap.domain.usecases.ObserveCityActivitiesUseCase
import com.example.helsinkimap.domain.usecases.ObserveGpsErrorUseCase
import com.example.helsinkimap.domain.usecases.ObserveLocationUseCase
import com.example.helsinkimap.specs.api.providers.DispatchersProvider
import com.example.helsinkimap.specs.api.repositories.CityEntertainmentRepositoryApi
import com.example.helsinkimap.specs.api.repositories.LocationRepositoryApi
import com.example.helsinkimap.specs.api.usecases.ObserveCityActivitiesUseCaseApi
import com.example.helsinkimap.specs.api.usecases.ObserveGpsErrorUseCaseApi
import com.example.helsinkimap.specs.api.usecases.ObserveLocationUseCaseApi
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

    @Provides
    fun provideObserveCityActivitiesUseCase(
        cityEntertainmentRepositoryApi: CityEntertainmentRepositoryApi,
        locationRepository: LocationRepositoryApi
    ): ObserveCityActivitiesUseCaseApi =
        ObserveCityActivitiesUseCase(
            cityEntertainmentRepositoryApi,
            locationRepository
        )

    @Provides
    fun provideObserveGpsErrorUseCase(
        locationRepository: LocationRepositoryApi
    ): ObserveGpsErrorUseCaseApi =
        ObserveGpsErrorUseCase(
            locationRepository
        )

    @Provides
    fun provideObserveLocationUseCase(
        locationRepository: LocationRepositoryApi
    ): ObserveLocationUseCaseApi =
        ObserveLocationUseCase(
            locationRepository
        )
}
