package com.example.helsinkimap.specs.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActivityDto(
    val id: String,
    val name: String,
    val infoUrl: String,
    val location: ActivityLocationDto,
    val description: String,
    val imageLinkList: List<ActivityImageLinkDto>
) : Parcelable
