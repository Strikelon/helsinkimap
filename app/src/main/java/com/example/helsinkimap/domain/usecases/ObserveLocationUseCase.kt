package com.example.helsinkimap.domain.usecases

import com.example.helsinkimap.data.dispatcher.DispatchersProvider
import com.example.helsinkimap.specs.api.repositories.LocationRepositoryApi
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ObserveLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepositoryApi,
    private val dispatchers: DispatchersProvider
) {

    operator fun invoke(): Flow<LatLng> =
        locationRepository.observeLocation()
            .distinctUntilChanged()
            .flowOn(dispatchers.io())
}
