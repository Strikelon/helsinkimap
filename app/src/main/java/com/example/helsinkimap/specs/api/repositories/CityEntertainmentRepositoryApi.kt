package com.example.helsinkimap.specs.api.repositories

import com.example.helsinkimap.specs.entity.ActivityDto
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single

interface CityEntertainmentRepositoryApi {

    fun getActivities(latLng: LatLng, range: Double): Single<List<ActivityDto>>
}
