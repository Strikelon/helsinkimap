package com.example.helsinkimap.domain.interactor

import com.example.helsinkimap.data.scheduler.SchedulersProvider
import com.example.helsinkimap.specs.api.interactors.MapInteractorApi
import com.example.helsinkimap.specs.api.repositories.CityEntertainmentRepositoryApi
import com.example.helsinkimap.specs.api.repositories.LocationRepositoryApi
import com.example.helsinkimap.specs.entity.ActivityDto
import com.example.helsinkimap.specs.entity.ErrorTypes
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable
import javax.inject.Inject

class MapInteractor @Inject constructor(
    private val cityEntertainmentRepositoryApi: CityEntertainmentRepositoryApi,
    private val locationRepository: LocationRepositoryApi,
    schedulers: SchedulersProvider,
) : BaseInteractor(schedulers), MapInteractorApi {

    override fun observeCityActivitiesFlowable(): Flowable<List<ActivityDto>> =
        locationRepository.observeLocationFlowable()
            .distinctUntilChanged()
            .switchMap { latLng: LatLng ->
                getActivities(latLng)
            }
            .schedule()

    override fun observeLocationFlowable(): Flowable<LatLng> =
        locationRepository.observeLocationFlowable()
            .distinctUntilChanged()
            .schedule()

    override fun observeGpsErrorFlowable(): Flowable<ErrorTypes> =
        locationRepository.observeGpsErrorFlowable()
            .distinctUntilChanged()
            .schedule()

    private fun getActivities(latLng: LatLng): Flowable<List<ActivityDto>> =
        cityEntertainmentRepositoryApi.getActivities(latLng, DEFAULT_ACTIVITIES_RANGE).toFlowable()
            .schedule()

    companion object {
        private const val DEFAULT_ACTIVITIES_RANGE = 1.0
    }
}
