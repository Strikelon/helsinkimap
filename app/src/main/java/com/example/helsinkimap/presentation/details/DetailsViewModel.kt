package com.example.helsinkimap.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.example.helsinkimap.presentation.arch.viewmodel.MvvmViewModel
import com.example.helsinkimap.presentation.arch.viewmodel.SingleLiveData
import com.example.helsinkimap.presentation.details.DetailsMvvmFragment.Companion.ARGUMENT_SELECTED_CITY_ACTIVITY
import com.example.helsinkimap.specs.entity.ActivityDto
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val poiEvent: LiveData<ActivityDto> by lazy { SingleLiveData() }

    init {
        cityActivityDto?.let {
            poiEvent.postValue(it)
        }
    }
}
