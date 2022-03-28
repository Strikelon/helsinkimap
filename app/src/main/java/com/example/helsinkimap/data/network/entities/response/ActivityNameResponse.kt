package com.example.helsinkimap.data.network.entities.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ActivityNameResponse(
    @Json(name = "fi")
    val nameFi: String,
)
