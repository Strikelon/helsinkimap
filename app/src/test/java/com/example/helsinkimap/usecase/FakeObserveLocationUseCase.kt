package com.example.helsinkimap.usecase

import com.example.helsinkimap.model.Models.HELSINKI_LAT_LNG
import com.example.helsinkimap.specs.api.exceptions.LocationDenyPermissionException
import com.example.helsinkimap.specs.api.usecases.ObserveLocationUseCaseApi
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class FakeObserveLocationUseCase(
    private val status: FakeObserveLocationUseCaseStatus = FakeObserveLocationUseCaseStatus.SUCCESS
) : ObserveLocationUseCaseApi {

    override fun invoke(): Flow<LatLng> =
        when (status) {
            FakeObserveLocationUseCaseStatus.SUCCESS -> {
                flow {
                    emit(HELSINKI_LAT_LNG)
                }
            }
            FakeObserveLocationUseCaseStatus.ERROR_LOCATION_DENY -> {
                flow {
                    throw LocationDenyPermissionException()
                }
            }
            FakeObserveLocationUseCaseStatus.ERROR -> {
                flow {
                    throw Exception()
                }
            }
        }

    enum class FakeObserveLocationUseCaseStatus {
        SUCCESS,
        ERROR_LOCATION_DENY,
        ERROR
    }
}
