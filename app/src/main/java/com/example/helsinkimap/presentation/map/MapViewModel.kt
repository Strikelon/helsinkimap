package com.example.helsinkimap.presentation.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.helsinkimap.presentation.arch.viewmodel.MvvmViewModel
import com.example.helsinkimap.presentation.arch.viewmodel.SingleLiveData
import com.example.helsinkimap.specs.api.exceptions.LocationDenyPermissionException
import com.example.helsinkimap.specs.api.interactors.MapInteractorApi
import com.example.helsinkimap.specs.entity.ActivityDto
import com.example.helsinkimap.specs.entity.ErrorTypes
import com.example.helsinkimap.specs.entity.NavigationEvent
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapInteractorApi: MapInteractorApi
) : MvvmViewModel() {

    private var cityActivitiesDtoList: List<ActivityDto> = listOf()
    private var selectedCityActivity: ActivityDto? = null

    val currentUserPositionEvent: LiveData<LatLng> by lazy { SingleLiveData() }
    val poiListEvent: LiveData<List<ActivityDto>> by lazy { SingleLiveData() }
    val detailsButtonVisibilityEvent: LiveData<Boolean> by lazy { MutableLiveData() }
    val errorEvent: LiveData<ErrorTypes> by lazy { SingleLiveData() }
    val navigationEvent: LiveData<NavigationEvent> by lazy { SingleLiveData() }

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
                    currentUserPositionEvent.postValue(
                        it
                    )
                },
                { throwable ->
                    Log.e("Error", "observeLocationFlowable() error $throwable")
                    if (throwable is LocationDenyPermissionException) {
                        errorEvent.postValue(ErrorTypes.PERMISSION_ERROR)
                    } else {
                        errorEvent.postValue(ErrorTypes.GPS_USE_ERROR)
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
                    poiListEvent.postValue(it)
                },
                {
                    Log.e("Error", "getActivities() error $it")
                    errorEvent.postValue(ErrorTypes.NETWORK_ERROR)
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
                        errorEvent.postValue(it)
                    },
                    {
                        Log.e("Error", "observeGpsErrorFlowable() error $it")
                        errorEvent.postValue(ErrorTypes.GPS_USE_ERROR)
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
                detailsButtonVisibilityEvent.postValue(true)
            }
        } ?: skipSelectedCityActivity()
    }

    fun skipSelectedCityActivity() {
        selectedCityActivity = null
        detailsButtonVisibilityEvent.postValue(false)
    }

    fun openDetailsScreen() {
        selectedCityActivity?.let { selectedCityActivityNotNull ->
            navigationEvent.postValue(NavigationEvent.OpenDetailsScreen(selectedCityActivityNotNull))
        }
    }

    companion object {
        private const val OBSERVE_GPS_ERROR_TAG = "observe_gps_error_tag"
    }
}
