package com.example.helsinkimap.data.network.entities.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ActivityAddressResponse(
    @Json(name = "street_address")
    val streetAddress: String?,
    @Json(name = "postal_code")
    val postalCode: String?,
    @Json(name = "locality")
    val locality: String?,
    @Json(name = "neighbourhood")
    val neighbourhood: String?
)
