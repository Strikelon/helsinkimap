package com.example.helsinkimap.specs.api.interactors

import com.example.helsinkimap.specs.entity.ActivityDto
import com.example.helsinkimap.specs.entity.ErrorTypes
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable

interface MapInteractorApi {

    fun observeCityActivitiesFlowable(): Flowable<List<ActivityDto>>

    fun observeLocationFlowable(): Flowable<LatLng>

    fun observeGpsErrorFlowable(): Flowable<ErrorTypes>
}
