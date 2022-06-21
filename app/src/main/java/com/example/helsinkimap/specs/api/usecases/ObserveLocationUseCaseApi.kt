package com.example.helsinkimap.specs.api.usecases

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface ObserveLocationUseCaseApi {

    operator fun invoke(): Flow<LatLng>
}
