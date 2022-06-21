package com.example.helsinkimap.specs.api.usecases

import com.example.helsinkimap.specs.entity.ErrorTypes
import kotlinx.coroutines.flow.Flow

interface ObserveGpsErrorUseCaseApi {

    operator fun invoke(): Flow<ErrorTypes>
}
