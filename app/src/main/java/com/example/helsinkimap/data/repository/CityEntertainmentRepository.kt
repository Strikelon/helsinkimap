package com.example.helsinkimap.data.repository

import com.example.helsinkimap.data.network.datasource.ActivitiesRemoteDatasource
import com.example.helsinkimap.data.network.entities.response.ActivityResponse
import com.example.helsinkimap.data.network.mapper.ActivityNetworkMapper
import com.example.helsinkimap.specs.api.repositories.CityEntertainmentRepositoryApi
import com.example.helsinkimap.specs.entity.ActivityDto
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class CityEntertainmentRepository @Inject constructor(
    private val activitiesRemoteDatasource: ActivitiesRemoteDatasource
) : CityEntertainmentRepositoryApi {

    override suspend fun getCityActivities(latLng: LatLng, range: Double): List<ActivityDto> {
        val activitiesResponse = activitiesRemoteDatasource.getCityActivities(latLng, range)
        val activityDtoList = mutableListOf<ActivityDto>()
        activitiesResponse.data.forEach { activityResponse: ActivityResponse ->
            activityDtoList.add(ActivityNetworkMapper.fromBusiness(activityResponse))
        }
        return activityDtoList
    }
}
