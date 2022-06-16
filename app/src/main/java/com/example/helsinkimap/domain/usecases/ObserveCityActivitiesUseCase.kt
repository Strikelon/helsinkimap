package com.example.helsinkimap.domain.usecases

import com.example.helsinkimap.data.dispatcher.DispatchersProvider
import com.example.helsinkimap.specs.api.repositories.CityEntertainmentRepositoryApi
import com.example.helsinkimap.specs.api.repositories.LocationRepositoryApi
import com.example.helsinkimap.specs.entity.ActivityDto
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ObserveCityActivitiesUseCase @Inject constructor(
    private val cityEntertainmentRepositoryApi: CityEntertainmentRepositoryApi,
    private val locationRepository: LocationRepositoryApi,
    private val dispatchers: DispatchersProvider
) {

    operator fun invoke(): Flow<List<ActivityDto>> =
        locationRepository.observeLocation()
            .distinctUntilChanged()
            .flatMapLatest { latLng: LatLng ->
                getCityActivities(latLng)
            }
            .flowOn(dispatchers.io())

    private suspend fun getCityActivities(latLng: LatLng): Flow<List<ActivityDto>> =
        flow {
            val result = cityEntertainmentRepositoryApi.getCityActivities(
                latLng = latLng,
                range = DEFAULT_ACTIVITIES_RANGE
            )
            emit(result)
        }

    companion object {
        private const val DEFAULT_ACTIVITIES_RANGE = 1.0
    }
}
