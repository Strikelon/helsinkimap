package com.example.helsinkimap.data.network.api

import com.example.helsinkimap.data.network.entities.response.ActivitiesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

    @GET(ACTIVITIES)
    suspend fun getCityActivities(@Query("distance_filter") distanceFilter: String): ActivitiesResponse

    companion object {
        private const val ACTIVITIES = "v1/activities/"
    }
}
