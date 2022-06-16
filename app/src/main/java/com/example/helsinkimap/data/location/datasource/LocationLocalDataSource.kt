package com.example.helsinkimap.data.location.datasource

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.example.helsinkimap.data.coroutinescope.CoroutineScopeProvider
import com.example.helsinkimap.specs.api.exceptions.LocationDenyPermissionException
import com.example.helsinkimap.specs.entity.ErrorTypes
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationLocalDataSource @Inject constructor(
    @ApplicationContext
    private val context: Context,
    coroutineScopeProvider: CoroutineScopeProvider
) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest.create()?.apply {
        interval = INTERVAL
        fastestInterval = FASTEST_INTERVAL
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private val mLocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val gpsErrorSharedFlow = MutableSharedFlow<ErrorTypes>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )

    init {
        coroutineScopeProvider.coroutineScope().launch {
            while (isActive) {
                delay(CHECK_ERRORS_INTERVAL)
                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    gpsErrorSharedFlow.emit(ErrorTypes.GPS_ENABLE_ERROR)
                } else {
                    gpsErrorSharedFlow.emit(ErrorTypes.NONE)
                }
            }
        }
    }

    fun observeLocation(): Flow<LatLng> =
        callbackFlow {
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations) {
                        location?.let {
                            trySend(mapLocationToLocationData(location))
                        }
                    }
                }
            }
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                throw LocationDenyPermissionException(ACCESS_FINE_LOCATION_DENIED)
            }
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            awaitClose { fusedLocationClient?.removeLocationUpdates(locationCallback) }
        }

    fun observeGpsError(): Flow<ErrorTypes> =
        gpsErrorSharedFlow.asSharedFlow()

    private fun mapLocationToLocationData(location: Location) =
        LatLng(
            location.latitude,
            location.longitude
        )

    companion object {
        private const val INTERVAL = 10000L
        private const val FASTEST_INTERVAL = 5000L
        private const val ACCESS_FINE_LOCATION_DENIED =
            "Access fine location denied"
        private const val CHECK_ERRORS_INTERVAL = 2000L
    }
}
