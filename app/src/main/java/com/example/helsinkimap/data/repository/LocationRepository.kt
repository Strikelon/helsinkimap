package com.example.helsinkimap.data.repository

import com.example.helsinkimap.data.location.datasource.LocationLocalDataSource
import com.example.helsinkimap.specs.api.repositories.LocationRepositoryApi
import com.example.helsinkimap.specs.entity.ErrorTypes
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationLocalDataSource: LocationLocalDataSource
) : LocationRepositoryApi {

    override fun observeGpsError(): Flow<ErrorTypes> =
        locationLocalDataSource.observeGpsError()

    override fun observeLocation(): Flow<LatLng> =
        locationLocalDataSource.observeLocation()

    override fun cacheLocation(latLng: LatLng) {
        locationLocalDataSource.cacheLocation(latLng)
    }

    override fun getCachedLocation(): LatLng? =
        locationLocalDataSource.getCachedLocation()
}
