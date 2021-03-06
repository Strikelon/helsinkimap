package com.example.helsinkimap.domain.usecases

import com.example.helsinkimap.core.mathext.calculateDistanceInKilometer
import com.example.helsinkimap.specs.api.repositories.CityEntertainmentRepositoryApi
import com.example.helsinkimap.specs.api.repositories.LocationRepositoryApi
import com.example.helsinkimap.specs.api.usecases.ObserveCityActivitiesUseCaseApi
import com.example.helsinkimap.specs.entity.ActivityDto
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObserveCityActivitiesUseCase @Inject constructor(
    private val cityEntertainmentRepositoryApi: CityEntertainmentRepositoryApi,
    private val locationRepository: LocationRepositoryApi
) : ObserveCityActivitiesUseCaseApi {

    override fun invoke(): Flow<List<ActivityDto>> =
        locationRepository.observeLocation()
            .filter { newLatLng: LatLng ->
                val lastCityActivitiesLocation = locationRepository.getCachedLocation()
                return@filter if (lastCityActivitiesLocation != null) {
                    val distance = newLatLng.calculateDistanceInKilometer(lastCityActivitiesLocation)
                    distance >= DEFAULT_ACTIVITIES_RANGE
                } else {
                    true
                }
            }
            .flatMapLatest { newLatLng: LatLng ->
                locationRepository.cacheLocation(newLatLng)
                getCityActivities(newLatLng)
            }

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
