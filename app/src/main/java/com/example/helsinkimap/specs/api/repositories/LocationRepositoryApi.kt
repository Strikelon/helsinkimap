package com.example.helsinkimap.specs.api.repositories

import com.example.helsinkimap.specs.entity.ErrorTypes
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationRepositoryApi {

    fun observeGpsError(): Flow<ErrorTypes>

    fun observeLocation(): Flow<LatLng>

    fun cacheLocation(latLng: LatLng)

    fun getCachedLocation(): LatLng?
}
