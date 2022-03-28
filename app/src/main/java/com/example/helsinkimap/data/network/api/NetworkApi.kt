package com.example.helsinkimap.data.network.api

import com.example.helsinkimap.data.network.entities.response.ActivitiesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

    @GET(ACTIVITIES)
    fun getActivities(@Query("distance_filter") distanceFilter: String): Single<ActivitiesResponse>

    companion object {
        private const val ACTIVITIES = "v1/activities/"
    }
}
