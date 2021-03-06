package com.example.helsinkimap.domain.usecases

import com.example.helsinkimap.specs.api.repositories.LocationRepositoryApi
import com.example.helsinkimap.specs.api.usecases.ObserveGpsErrorUseCaseApi
import com.example.helsinkimap.specs.entity.ErrorTypes
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveGpsErrorUseCase @Inject constructor(
    private val locationRepository: LocationRepositoryApi,
) : ObserveGpsErrorUseCaseApi {

    override fun invoke(): Flow<ErrorTypes> =
        locationRepository.observeGpsError()
}
