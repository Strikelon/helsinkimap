package com.example.helsinkimap.data.network.mapper

import com.example.helsinkimap.data.network.entities.response.ActivityDescriptionResponse
import com.example.helsinkimap.data.network.mapper.ActivityDescriptionNetworkMapper.EMPTY
import com.example.helsinkimap.data.network.mapper.ActivityDescriptionNetworkMapper.N_TAG
import com.example.helsinkimap.data.network.mapper.ActivityDescriptionNetworkMapper.P_TAG_CLOSE
import com.example.helsinkimap.data.network.mapper.ActivityDescriptionNetworkMapper.P_TAG_OPEN
import com.example.helsinkimap.domain.common.Mapper

object ActivityDescriptionNetworkMapper : Mapper<ActivityDescriptionResponse, String>(
    fromBusinessMapper = { activityDescriptionResponse: ActivityDescriptionResponse ->
        val description = (activityDescriptionResponse.body ?: EMPTY)
            .replace(P_TAG_OPEN, EMPTY)
            .replace(P_TAG_CLOSE, EMPTY)
            .replace(N_TAG, EMPTY)
        description
    }
) {
    private const val EMPTY = ""
    private const val P_TAG_OPEN = "<p>"
    private const val P_TAG_CLOSE = "</p>"
    private const val N_TAG = "\n"
}
