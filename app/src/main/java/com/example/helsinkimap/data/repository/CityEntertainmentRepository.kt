package com.example.helsinkimap.data.repository

import com.example.helsinkimap.data.network.datasource.NetworkApiDatasource
import com.example.helsinkimap.specs.api.repositories.CityEntertainmentRepositoryApi
import com.example.helsinkimap.specs.entity.ActivityDto
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import javax.inject.Inject

class CityEntertainmentRepository @Inject constructor(
    private val networkApiDatasource: NetworkApiDatasource
) : CityEntertainmentRepositoryApi {

    override fun getActivities(latLng: LatLng, range: Double): Single<List<ActivityDto>> =
        networkApiDatasource.getActivities(latLng, range)
}
