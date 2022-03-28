package com.example.helsinkimap.data.network.datasource

import com.example.helsinkimap.data.network.api.NetworkApi
import com.example.helsinkimap.data.network.entities.response.ActivitiesResponse
import com.example.helsinkimap.data.network.entities.response.ActivityResponse
import com.example.helsinkimap.data.network.mapper.ActivityNetworkMapper
import com.example.helsinkimap.specs.entity.ActivityDto
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkApiDatasource @Inject constructor(
    private val networkApi: NetworkApi
) {
    fun getActivities(latLng: LatLng, range: Double): Single<List<ActivityDto>> {
        val getCityActivitiesQuery = "${latLng.latitude},${latLng.longitude},$range"
        return networkApi.getActivities(getCityActivitiesQuery)
            .toObservable()
            .flatMap { activitiesResponse: ActivitiesResponse ->
                Observable.fromIterable(activitiesResponse.data)
            }
            .map { activityResponse: ActivityResponse ->
                ActivityNetworkMapper.fromBusiness(activityResponse)
            }
            .toList()
    }
}
