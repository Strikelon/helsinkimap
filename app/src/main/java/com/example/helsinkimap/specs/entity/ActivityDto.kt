package com.example.helsinkimap.specs.entity

data class ActivityDto(
    val id: String,
    val name: String,
    val infoUrl: String,
    val location: ActivityLocationDto,
    val description: String,
    val imageLinkList: List<ActivityImageLinkDto>
)
