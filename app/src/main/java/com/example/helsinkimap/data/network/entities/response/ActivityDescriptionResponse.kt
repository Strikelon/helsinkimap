package com.example.helsinkimap.data.network.entities.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ActivityDescriptionResponse(
    @Json(name = "body")
    val body: String?,
    @Json(name = "images")
    val images: List<ActivityImageResponse>
)
