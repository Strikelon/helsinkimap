package com.example.helsinkimap.data.repository

import com.example.helsinkimap.data.location.datasource.LocationDataSource
import com.example.helsinkimap.specs.api.repositories.LocationRepositoryApi
import com.example.helsinkimap.specs.entity.ErrorTypes
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationDataSource: LocationDataSource
) : LocationRepositoryApi {

    override fun observeLocationFlowable(): Flowable<LatLng> =
        locationDataSource.observeLocationFlowable()

    override fun observeGpsErrorFlowable(): Flowable<ErrorTypes> =
        locationDataSource.observeGpsErrorFlowable()
}
