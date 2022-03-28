package com.example.helsinkimap.data.network.mapper

import com.example.helsinkimap.data.network.entities.response.ActivityResponse
import com.example.helsinkimap.data.network.mapper.ActivityNetworkMapper.EMPTY
import com.example.helsinkimap.domain.common.Mapper
import com.example.helsinkimap.specs.entity.ActivityDto

object ActivityNetworkMapper : Mapper<ActivityResponse, ActivityDto>(
    fromBusinessMapper = { activityResponse: ActivityResponse ->
        ActivityDto(
            id = activityResponse.id,
            name = ActivityNameNetworkMapper.fromBusiness(activityResponse.name),
            infoUrl = activityResponse.infoUrl ?: EMPTY,
            location = ActivityLocationNetworkMapper.fromBusiness(activityResponse.location),
            description = ActivityDescriptionNetworkMapper.fromBusiness(activityResponse.description),
            imageLinkList = ActivityImagesNetworkMapper.fromBusiness(activityResponse.description.images)
        )
    }
) {
    private const val EMPTY = ""
}
