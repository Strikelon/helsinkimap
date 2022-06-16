package com.example.helsinkimap.specs.api.repositories

import com.example.helsinkimap.specs.entity.ActivityDto
import com.google.android.gms.maps.model.LatLng

interface CityEntertainmentRepositoryApi {

    suspend fun getCityActivities(latLng: LatLng, range: Double): List<ActivityDto>
}
