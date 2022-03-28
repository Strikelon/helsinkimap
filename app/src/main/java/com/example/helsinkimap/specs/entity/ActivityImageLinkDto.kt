package com.example.helsinkimap.specs.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActivityImageLinkDto(
    val url: String
) : Parcelable
