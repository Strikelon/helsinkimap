package com.example.helsinkimap.specs.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActivityLocationDto(
    val lat: Double,
    val lon: Double,
    val locality: String,
    val postalCode: String,
    val streetAddress: String
) : Parcelable
