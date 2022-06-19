package com.example.helsinkimap.core.mathext

import com.google.android.gms.maps.model.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

const val AVERAGE_RADIUS_OF_EARTH_KM = 6371

fun LatLng.calculateDistanceInKilometer(venueLatLng: LatLng): Double {

    val latDistance = Math.toRadians(this.latitude - venueLatLng.latitude)
    val lngDistance = Math.toRadians(this.longitude - venueLatLng.longitude)

    val a = (
        sin(latDistance / 2) * sin(latDistance / 2) +
            (
                cos(Math.toRadians(this.latitude)) * cos(Math.toRadians(venueLatLng.latitude)) *
                    sin(lngDistance / 2) * sin(lngDistance / 2)
                )
        )

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return (AVERAGE_RADIUS_OF_EARTH_KM * c)
}
