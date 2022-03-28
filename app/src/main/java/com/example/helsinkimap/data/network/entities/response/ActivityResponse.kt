package com.example.helsinkimap.data.network.entities.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ActivityResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: ActivityNameResponse,
    @Json(name = "info_url")
    val infoUrl: String?,
    @Json(name = "location")
    val location: ActivityLocationResponse,
    @Json(name = "description")
    val description: ActivityDescriptionResponse
)
