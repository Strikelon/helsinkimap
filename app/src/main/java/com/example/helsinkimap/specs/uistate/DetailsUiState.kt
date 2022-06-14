package com.example.helsinkimap.specs.uistate

import com.example.helsinkimap.specs.entity.ActivityDto

data class DetailsUiState (
    val cityActivityDto: ActivityDto? = null,
    val error: Boolean = false
)
