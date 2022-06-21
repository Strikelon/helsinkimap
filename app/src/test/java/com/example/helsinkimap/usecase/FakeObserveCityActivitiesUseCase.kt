package com.example.helsinkimap.usecase

import com.example.helsinkimap.model.Models
import com.example.helsinkimap.specs.api.usecases.ObserveCityActivitiesUseCaseApi
import com.example.helsinkimap.specs.entity.ActivityDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class FakeObserveCityActivitiesUseCase(
    private val listActivityDto: List<ActivityDto>? = Models.listActivityDto
) : ObserveCityActivitiesUseCaseApi {

    override fun invoke(): Flow<List<ActivityDto>> {
        return listActivityDto?.let {
            flow {
                emit(it)
            }
        } ?: flow {
            throw Exception()
        }
    }
}
