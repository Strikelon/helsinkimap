package com.example.helsinkimap.domain.usecases

import com.example.helsinkimap.data.dispatcher.DispatchersProvider
import com.example.helsinkimap.specs.api.repositories.LocationRepositoryApi
import com.example.helsinkimap.specs.entity.ErrorTypes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ObserveGpsErrorUseCase @Inject constructor(
    private val locationRepository: LocationRepositoryApi,
    private val dispatchers: DispatchersProvider
) {

    operator fun invoke(): Flow<ErrorTypes> =
        locationRepository.observeGpsError()
            .distinctUntilChanged()
            .flowOn(dispatchers.io())
}
