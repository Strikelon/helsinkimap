package com.example.helsinkimap.presentation.map

import android.util.Log
import com.example.helsinkimap.presentation.arch.viewmodel.MvvmViewModel
import com.example.helsinkimap.specs.api.exceptions.LocationDenyPermissionException
import com.example.helsinkimap.specs.api.interactors.MapInteractorApi
import com.example.helsinkimap.specs.entity.ActivityDto
import com.example.helsinkimap.specs.entity.ErrorTypes
import com.example.helsinkimap.specs.entity.NavigationEvent
import com.example.helsinkimap.specs.uistate.MapUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapInteractorApi: MapInteractorApi
) : MvvmViewModel() {

    private var cityActivitiesDtoList: List<ActivityDto> = listOf()
    private var selectedCityActivity: ActivityDto? = null

    private val _uiState = MutableStateFlow(MapUIState())
    val uiState: StateFlow<MapUIState> = _uiState

    init {
        observeLocationFlowable()
        observeCityActivitiesFlowable()
    }

    override fun attach() {
        super.attach()
        observeGpsErrorFlowable()
    }

    override fun detach() {
        super.detach()
        unsubscribeGpsErrorFlowable()
    }

    private fun observeLocationFlowable() {
        mapInteractorApi.observeLocationFlowable()
            .subscribe(
                {
                    _uiState.update { currentState: MapUIState ->
                        currentState.copy(
                            currentUserPosition = it
                        )
                    }
                },
                { throwable ->
                    Log.e("Error", "observeLocationFlowable() error $throwable")
                    if (throwable is LocationDenyPermissionException) {
                        _uiState.update { currentState: MapUIState ->
                            currentState.copy(
                                error = ErrorTypes.PERMISSION_ERROR
                            )
                        }
                    } else {
                        _uiState.update { currentState: MapUIState ->
                            currentState.copy(
                                error = ErrorTypes.GPS_USE_ERROR
                            )
                        }
                    }
                }
            )
            .unsubscribeOnDestroy()
    }

    private fun observeCityActivitiesFlowable() {
        mapInteractorApi.observeCityActivitiesFlowable()
            .subscribe(
                {
                    skipSelectedCityActivity()
                    cityActivitiesDtoList = it
                    _uiState.update { currentState: MapUIState ->
                        currentState.copy(
                            poiList = it
                        )
                    }
                },
                {
                    Log.e("Error", "getActivities() error $it")
                    _uiState.update { currentState: MapUIState ->
                        currentState.copy(
                            error = ErrorTypes.NETWORK_ERROR
                        )
                    }
                }
            )
            .unsubscribeOnDestroy()
    }

    private fun observeGpsErrorFlowable() {
        rxHelper.unsubscribeBy(OBSERVE_GPS_ERROR_TAG)
        rxHelper.add(
            OBSERVE_GPS_ERROR_TAG,
            mapInteractorApi.observeGpsErrorFlowable()
                .subscribe(
                    {
                        _uiState.update { currentState: MapUIState ->
                            currentState.copy(
                                error = it
                            )
                        }
                    },
                    {
                        Log.e("Error", "observeGpsErrorFlowable() error $it")
                        _uiState.update { currentState: MapUIState ->
                            currentState.copy(
                                error = ErrorTypes.GPS_USE_ERROR
                            )
                        }
                    }
                )
        )
    }

    private fun unsubscribeGpsErrorFlowable() {
        rxHelper.unsubscribeBy(OBSERVE_GPS_ERROR_TAG)
    }

    fun selectPoiMarker(id: String?) {
        id?.let { selectedCityActivityId ->
            selectedCityActivity = cityActivitiesDtoList.find { cityActivity ->
                selectedCityActivityId == cityActivity.id
            }
            selectedCityActivity?.let {
                _uiState.update { currentState: MapUIState ->
                    currentState.copy(
                        detailsButtonVisibility = true
                    )
                }
            }
        } ?: skipSelectedCityActivity()
    }

    fun skipSelectedCityActivity() {
        selectedCityActivity = null
        _uiState.update { currentState: MapUIState ->
            currentState.copy(
                detailsButtonVisibility = false
            )
        }
    }

    fun openDetailsScreen() {
        selectedCityActivity?.let { selectedCityActivityNotNull ->
            _uiState.update { currentState: MapUIState ->
                currentState.copy(
                    navigation = NavigationEvent.OpenDetailsScreen(selectedCityActivityNotNull)
                )
            }
        }
    }

    fun navigationEventHandled() {
        _uiState.update { currentState: MapUIState ->
            currentState.copy(
                navigation = null
            )
        }
    }

    fun errorHandled() {
        _uiState.update { currentState: MapUIState ->
            currentState.copy(
                error = null
            )
        }
    }

    companion object {
        private const val OBSERVE_GPS_ERROR_TAG = "observe_gps_error_tag"
    }
}
