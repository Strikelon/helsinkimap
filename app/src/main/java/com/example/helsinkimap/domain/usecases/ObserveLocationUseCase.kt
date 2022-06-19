package com.example.helsinkimap.domain.usecases

import com.example.helsinkimap.specs.api.repositories.LocationRepositoryApi
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepositoryApi,
) {

    operator fun invoke(): Flow<LatLng> =
        locationRepository.observeLocation()
}
