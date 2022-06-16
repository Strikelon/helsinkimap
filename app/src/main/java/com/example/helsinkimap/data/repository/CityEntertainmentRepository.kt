package com.example.helsinkimap.data.repository

import com.example.helsinkimap.data.network.datasource.ActivitiesRemoteDatasource
import com.example.helsinkimap.specs.api.repositories.CityEntertainmentRepositoryApi
import com.example.helsinkimap.specs.entity.ActivityDto
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class CityEntertainmentRepository @Inject constructor(
    private val activitiesRemoteDatasource: ActivitiesRemoteDatasource
) : CityEntertainmentRepositoryApi {

    override suspend fun getCityActivities(latLng: LatLng, range: Double): List<ActivityDto> =
        activitiesRemoteDatasource.getCityActivities(latLng, range)
}
