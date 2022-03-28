package com.example.helsinkimap.data.network.mapper

import com.example.helsinkimap.data.network.entities.response.ActivityLocationResponse
import com.example.helsinkimap.data.network.mapper.ActivityLocationNetworkMapper.EMPTY
import com.example.helsinkimap.domain.common.Mapper
import com.example.helsinkimap.specs.entity.ActivityLocationDto

object ActivityLocationNetworkMapper : Mapper<ActivityLocationResponse, ActivityLocationDto>(
    fromBusinessMapper = { activityLocationResponse: ActivityLocationResponse ->
        ActivityLocationDto(
            lat = activityLocationResponse.lat,
            lon = activityLocationResponse.lon,
            locality = activityLocationResponse.address.locality ?: EMPTY,
            postalCode = activityLocationResponse.address.postalCode ?: EMPTY,
            streetAddress = activityLocationResponse.address.streetAddress ?: EMPTY
        )
    }
) {
    private const val EMPTY = ""
}
