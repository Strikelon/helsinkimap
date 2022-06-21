package com.example.helsinkimap.specs.api.usecases

import com.example.helsinkimap.specs.entity.ActivityDto
import kotlinx.coroutines.flow.Flow

interface ObserveCityActivitiesUseCaseApi {

    operator fun invoke(): Flow<List<ActivityDto>>
}
