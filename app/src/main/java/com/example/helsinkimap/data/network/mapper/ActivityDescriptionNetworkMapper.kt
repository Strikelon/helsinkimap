package com.example.helsinkimap.data.network.mapper

import com.example.helsinkimap.data.network.entities.response.ActivityDescriptionResponse
import com.example.helsinkimap.data.network.mapper.ActivityDescriptionNetworkMapper.EMPTY
import com.example.helsinkimap.domain.common.Mapper

object ActivityDescriptionNetworkMapper : Mapper<ActivityDescriptionResponse, String>(
    fromBusinessMapper = { activityDescriptionResponse: ActivityDescriptionResponse ->
        val description = (activityDescriptionResponse.body ?: EMPTY)
        description
    }
) {
    private const val EMPTY = ""
}
