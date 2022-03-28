package com.example.helsinkimap.data.network.mapper

import com.example.helsinkimap.data.network.entities.response.ActivityNameResponse
import com.example.helsinkimap.domain.common.Mapper

object ActivityNameNetworkMapper : Mapper<ActivityNameResponse, String>(
    fromBusinessMapper = { activityNameResponse: ActivityNameResponse ->
        activityNameResponse.nameFi
    }
)
