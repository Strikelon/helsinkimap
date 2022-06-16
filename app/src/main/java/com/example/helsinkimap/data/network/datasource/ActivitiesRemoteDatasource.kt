package com.example.helsinkimap.data.network.datasource

import com.example.helsinkimap.data.network.api.NetworkApi
import com.example.helsinkimap.data.network.entities.response.ActivityResponse
import com.example.helsinkimap.data.network.mapper.ActivityNetworkMapper
import com.example.helsinkimap.specs.entity.ActivityDto
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivitiesRemoteDatasource @Inject constructor(
    private val networkApi: NetworkApi
) {
    suspend fun getCityActivities(latLng: LatLng, range: Double): List<ActivityDto> {
        val getCityActivitiesQuery = "${latLng.latitude},${latLng.longitude},$range"
        val activitiesResponse = networkApi.getCityActivities(getCityActivitiesQuery)
        val activityDtoList = mutableListOf<ActivityDto>()
        activitiesResponse.data.forEach { activityResponse: ActivityResponse ->
            activityDtoList.add(ActivityNetworkMapper.fromBusiness(activityResponse))
        }
        return activityDtoList
    }
}
