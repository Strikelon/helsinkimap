package com.example.helsinkimap.data.location.datasource

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.example.helsinkimap.core.rx.RxHelperUpdated
import com.example.helsinkimap.specs.api.exceptions.LocationDenyPermissionException
import com.example.helsinkimap.specs.entity.ErrorTypes
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationDataSource @Inject constructor(
    private val context: Context
) {
    private val gpsErrorRelay: PublishRelay<ErrorTypes> = PublishRelay.create()
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest.create()?.apply {
        interval = INTERVAL
        fastestInterval = FASTEST_INTERVAL
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private val rxHelperUpdated: RxHelperUpdated = RxHelperUpdated()
    private val mLocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    init {
        rxHelperUpdated.add(
            Observable.interval(CHECK_ERRORS_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe {
                    if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        gpsErrorRelay.accept(ErrorTypes.GPS_ENABLE_ERROR)
                    } else {
                        gpsErrorRelay.accept(ErrorTypes.NONE)
                    }
                }
        )
    }

    fun observeLocationFlowable(): Flowable<LatLng> =
        Flowable.create<LatLng>({ emitter ->
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult ?: return
                    for (location in locationResult.locations) {
                        location?.let {
                            emitter.onNext(mapLocationToLocationData(location))
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
                emitter.onError(LocationDenyPermissionException(ACCESS_FINE_LOCATION_DENIED))
            }
            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            emitter.setCancellable {
                fusedLocationClient?.removeLocationUpdates(locationCallback)
            }
        }, BackpressureStrategy.BUFFER)
            .replay(1).refCount()

    fun observeGpsErrorFlowable(): Flowable<ErrorTypes> =
        gpsErrorRelay.toFlowable(
            BackpressureStrategy.LATEST
        ).publish().refCount()

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
