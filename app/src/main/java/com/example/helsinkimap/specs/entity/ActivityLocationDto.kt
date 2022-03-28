package com.example.helsinkimap.specs.entity

data class ActivityLocationDto(
    val lat: Double,
    val lon: Double,
    val locality: String,
    val postalCode: String,
    val streetAddress: String
)
