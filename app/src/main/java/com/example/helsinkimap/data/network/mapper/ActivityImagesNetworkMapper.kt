package com.example.helsinkimap.data.network.mapper

import com.example.helsinkimap.data.network.entities.response.ActivityImageResponse
import com.example.helsinkimap.data.network.mapper.ActivityImagesNetworkMapper.EMPTY
import com.example.helsinkimap.domain.common.Mapper
import com.example.helsinkimap.specs.entity.ActivityImageLinkDto

object ActivityImagesNetworkMapper :
    Mapper<List<ActivityImageResponse>, List<ActivityImageLinkDto>>(
        fromBusinessMapper = { activityImageResponseList: List<ActivityImageResponse> ->
            val activityImageLinkDtoList: MutableList<ActivityImageLinkDto> = ArrayList()
            activityImageResponseList.forEach { activityImageResponse ->
                activityImageLinkDtoList.add(
                    ActivityImageLinkDto(
                        url = activityImageResponse.url ?: EMPTY
                    )
                )
            }
            activityImageLinkDtoList
        }
    ) {
    private const val EMPTY = ""
}
