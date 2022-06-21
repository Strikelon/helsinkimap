package com.example.helsinkimap.usecase

import com.example.helsinkimap.specs.api.usecases.ObserveGpsErrorUseCaseApi
import com.example.helsinkimap.specs.entity.ErrorTypes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeObserveGpsErrorUseCase(
    private val errorType: ErrorTypes = ErrorTypes.NONE
) : ObserveGpsErrorUseCaseApi {

    override fun invoke(): Flow<ErrorTypes> =
        flow {
            emit(errorType)
        }
}
