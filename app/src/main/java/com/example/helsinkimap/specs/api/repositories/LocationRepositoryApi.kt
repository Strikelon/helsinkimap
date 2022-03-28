package com.example.helsinkimap.specs.api.repositories

import com.example.helsinkimap.specs.entity.ErrorTypes
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable

interface LocationRepositoryApi {

    fun observeLocationFlowable(): Flowable<LatLng>

    fun observeGpsErrorFlowable(): Flowable<ErrorTypes>
}
