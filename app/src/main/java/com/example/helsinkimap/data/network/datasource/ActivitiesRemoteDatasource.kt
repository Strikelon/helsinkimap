package com.example.helsinkimap.data.network.datasource

import com.example.helsinkimap.data.network.api.NetworkApi
import com.example.helsinkimap.data.network.entities.response.ActivitiesResponse
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivitiesRemoteDatasource @Inject constructor(
    private val networkApi: NetworkApi
) {
    suspend fun getCityActivities(latLng: LatLng, range: Double): ActivitiesResponse {
        val getCityActivitiesQuery = "${latLng.latitude},${latLng.longitude},$range"
        return networkApi.getCityActivities(getCityActivitiesQuery)
    }
}
