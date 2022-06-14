package com.example.helsinkimap.specs.uistate

import com.example.helsinkimap.specs.entity.ActivityDto
import com.example.helsinkimap.specs.entity.ErrorTypes
import com.example.helsinkimap.specs.entity.NavigationEvent
import com.google.android.gms.maps.model.LatLng

data class MapUIState(
    val currentUserPosition: LatLng? = null,
    val poiList: List<ActivityDto> = emptyList(),
    val detailsButtonVisibility: Boolean = false,
    val error: ErrorTypes? = null,
    val navigation: NavigationEvent? = null
)
