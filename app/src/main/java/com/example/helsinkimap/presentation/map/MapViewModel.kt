package com.example.helsinkimap.presentation.map

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.helsinkimap.domain.usecases.ObserveCityActivitiesUseCase
import com.example.helsinkimap.domain.usecases.ObserveGpsErrorUseCase
import com.example.helsinkimap.domain.usecases.ObserveLocationUseCase
import com.example.helsinkimap.presentation.arch.viewmodel.MvvmViewModel
import com.example.helsinkimap.specs.api.exceptions.LocationDenyPermissionException
import com.example.helsinkimap.specs.entity.ActivityDto
import com.example.helsinkimap.specs.entity.ErrorTypes
import com.example.helsinkimap.specs.entity.NavigationEvent
import com.example.helsinkimap.specs.uistate.MapUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val observeCityActivitiesUseCase: ObserveCityActivitiesUseCase,
    private val observeGpsErrorUseCase: ObserveGpsErrorUseCase,
    private val observeLocationUseCase: ObserveLocationUseCase,
) : MvvmViewModel() {

    private var cityActivitiesDtoList: List<ActivityDto> = listOf()
    private var selectedCityActivity: ActivityDto? = null

    private val _uiState = MutableStateFlow(MapUIState())
    val uiState: StateFlow<MapUIState> = _uiState

    init {
        observeLocation()
        observeCityActivities()
        observeGpsError()
    }

    private fun observeLocation() {
        viewModelScope.launch {
            try {
                observeLocationUseCase()
                    .collect {
                        _uiState.update { currentState: MapUIState ->
                            currentState.copy(
                                currentUserPosition = it
                            )
                        }
                    }
            } catch (e: Exception) {
                Log.e("Error", "observeLocationFlowable() error $e")
                if (e is LocationDenyPermissionException) {
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
        }
    }

    private fun observeCityActivities() {
        viewModelScope.launch {
            try {
                observeCityActivitiesUseCase()
                    .collect {
                        skipSelectedCityActivity()
                        cityActivitiesDtoList = it
                        _uiState.update { currentState: MapUIState ->
                            currentState.copy(
                                poiList = it
                            )
                        }
                    }
            } catch (e: Exception) {
                Log.e("Error", "getActivities() error $e")
                _uiState.update { currentState: MapUIState ->
                    currentState.copy(
                        error = ErrorTypes.NETWORK_ERROR
                    )
                }
            }
        }
    }

    private fun observeGpsError() {
        viewModelScope.launch {
            try {
                observeGpsErrorUseCase()
                    .collect {
                        Log.e("Error", "observeGpsErrorUseCase() $it")
                        _uiState.update { currentState: MapUIState ->
                            currentState.copy(
                                error = it
                            )
                        }
                    }
            } catch (e: Exception) {
                Log.e("Error", "observeGpsErrorUseCase() error $e")
                _uiState.update { currentState: MapUIState ->
                    currentState.copy(
                        error = ErrorTypes.GPS_USE_ERROR
                    )
                }
            }
        }
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
}
