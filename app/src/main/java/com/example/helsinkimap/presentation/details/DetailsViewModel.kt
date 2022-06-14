package com.example.helsinkimap.presentation.details

import androidx.lifecycle.SavedStateHandle
import com.example.helsinkimap.presentation.arch.viewmodel.MvvmViewModel
import com.example.helsinkimap.specs.entity.ActivityDto
import com.example.helsinkimap.specs.uistate.DetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : MvvmViewModel() {

    private val cityActivityDto: ActivityDto? by lazy {
        savedStateHandle.get<ActivityDto>(
            ARGUMENT_SELECTED_CITY_ACTIVITY
        )
    }

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState

    init {
        cityActivityDto?.let { cityActivityDtoNotNull: ActivityDto ->
            _uiState.update { currentUiState ->
                currentUiState.copy(cityActivityDto = cityActivityDtoNotNull)
            }
        } ?: _uiState.update { currentUiState ->
            currentUiState.copy(error = true)
        }
    }

    companion object {
        private const val ARGUMENT_SELECTED_CITY_ACTIVITY = "argument_selected_city_activity"
    }
}
