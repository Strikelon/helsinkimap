package com.example.helsinkimap.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.helsinkimap.domain.interactor.MapInteractor
import com.example.helsinkimap.domain.navigation.MainRouter
import com.example.helsinkimap.presentation.map.MapViewModel
import com.example.helsinkimap.specs.api.exceptions.LocationDenyPermissionException
import com.example.helsinkimap.specs.entity.ActivityDto
import com.example.helsinkimap.specs.entity.ActivityLocationDto
import com.example.helsinkimap.specs.entity.ErrorTypes
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var mapViewModel: MapViewModel

    @Mock
    lateinit var mapInteractor: MapInteractor

    @Mock
    lateinit var mainRouter: MainRouter

    @Mock
    lateinit var currentUserPositionObserver: Observer<LatLng>

    @Mock
    lateinit var poiListObserver: Observer<List<ActivityDto>>

    @Mock
    lateinit var detailsButtonVisibilityObserver: Observer<Boolean>

    @Mock
    lateinit var errorObserver: Observer<ErrorTypes>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun observeLocationAndCityActivitiesFlowableTest() {
        Mockito.`when`(mapInteractor.observeLocationFlowable())
            .thenReturn(Flowable.just(getHelsinkiLatLng()))
        Mockito.`when`(mapInteractor.observeCityActivitiesFlowable())
            .thenReturn(Flowable.just(getCityActivityList()))
        mapViewModel = MapViewModel(mapInteractor, mainRouter).apply {
            currentUserPositionEvent.observeForever(currentUserPositionObserver)
            poiListEvent.observeForever(poiListObserver)
        }
        Mockito.verify(currentUserPositionObserver).onChanged(getHelsinkiLatLng())
        Mockito.verify(poiListObserver).onChanged(getCityActivityList())
    }

    @Test
    fun observeLocationFlowableLocationDenyPermissionExceptionTest() {
        Mockito.`when`(mapInteractor.observeLocationFlowable())
            .thenReturn(Flowable.error(LocationDenyPermissionException("error")))
        Mockito.`when`(mapInteractor.observeCityActivitiesFlowable())
            .thenReturn(Flowable.just(getCityActivityList()))
        mapViewModel = MapViewModel(mapInteractor, mainRouter).apply {
            errorEvent.observeForever(errorObserver)
        }
        Mockito.verify(errorObserver).onChanged(ErrorTypes.PERMISSION_ERROR)
    }

    @Test
    fun observeLocationFlowableLocationDenyExceptionTest() {
        Mockito.`when`(mapInteractor.observeLocationFlowable())
            .thenReturn(Flowable.error(Exception("error")))
        Mockito.`when`(mapInteractor.observeCityActivitiesFlowable())
            .thenReturn(Flowable.just(getCityActivityList()))
        mapViewModel = MapViewModel(mapInteractor, mainRouter).apply {
            errorEvent.observeForever(errorObserver)
        }
        Mockito.verify(errorObserver).onChanged(ErrorTypes.GPS_USE_ERROR)
    }

    @Test
    fun observeCityActivitiesFlowableExceptionTest() {
        Mockito.`when`(mapInteractor.observeLocationFlowable())
            .thenReturn(Flowable.just(getHelsinkiLatLng()))
        Mockito.`when`(mapInteractor.observeCityActivitiesFlowable())
            .thenReturn(Flowable.error(Exception("error")))
        mapViewModel = MapViewModel(mapInteractor, mainRouter).apply {
            errorEvent.observeForever(errorObserver)
        }
        Mockito.verify(errorObserver).onChanged(ErrorTypes.NETWORK_ERROR)
    }

    @Test
    fun observeGpsErrorFlowableNoneErrorTest() {
        Mockito.`when`(mapInteractor.observeLocationFlowable())
            .thenReturn(Flowable.just(getHelsinkiLatLng()))
        Mockito.`when`(mapInteractor.observeCityActivitiesFlowable())
            .thenReturn(Flowable.just(getCityActivityList()))
        Mockito.`when`(mapInteractor.observeGpsErrorFlowable())
            .thenReturn(Flowable.just(ErrorTypes.NONE))
        mapViewModel = MapViewModel(mapInteractor, mainRouter).apply {
            errorEvent.observeForever(errorObserver)
            attach()
        }
        Mockito.verify(errorObserver).onChanged(ErrorTypes.NONE)
    }

    @Test
    fun observeGpsErrorFlowableErrorTest() {
        Mockito.`when`(mapInteractor.observeLocationFlowable())
            .thenReturn(Flowable.just(getHelsinkiLatLng()))
        Mockito.`when`(mapInteractor.observeCityActivitiesFlowable())
            .thenReturn(Flowable.just(getCityActivityList()))
        Mockito.`when`(mapInteractor.observeGpsErrorFlowable())
            .thenReturn(Flowable.error(Exception("error")))
        mapViewModel = MapViewModel(mapInteractor, mainRouter).apply {
            errorEvent.observeForever(errorObserver)
            attach()
        }
        Mockito.verify(errorObserver).onChanged(ErrorTypes.GPS_USE_ERROR)
    }

    @Test
    fun openDetailsScreenSuccessTest() {
        Mockito.`when`(mapInteractor.observeLocationFlowable())
            .thenReturn(Flowable.just(getHelsinkiLatLng()))
        Mockito.`when`(mapInteractor.observeCityActivitiesFlowable())
            .thenReturn(Flowable.just(getCityActivityList()))
        mapViewModel = MapViewModel(mapInteractor, mainRouter).apply {
            detailsButtonVisibilityEvent.observeForever(detailsButtonVisibilityObserver)
        }
        Mockito.verify(detailsButtonVisibilityObserver).onChanged(false)
        mapViewModel.selectPoiMarker(EXISTING_CITY_ACTIVITY_ID)
        Mockito.verify(detailsButtonVisibilityObserver).onChanged(true)
        mapViewModel.openDetailsScreen()
        Mockito.verify(mainRouter).openDetailsScreen(getActivityDto())
    }

    @Test
    fun skipSelectedCityActivityTest() {
        Mockito.`when`(mapInteractor.observeLocationFlowable())
            .thenReturn(Flowable.just(getHelsinkiLatLng()))
        Mockito.`when`(mapInteractor.observeCityActivitiesFlowable())
            .thenReturn(Flowable.just(getCityActivityList()))
        mapViewModel = MapViewModel(mapInteractor, mainRouter).apply {
            detailsButtonVisibilityEvent.observeForever(detailsButtonVisibilityObserver)
        }
        mapViewModel.skipSelectedCityActivity()
        Mockito.verify(detailsButtonVisibilityObserver, Mockito.times(2)).onChanged(false)
    }

    @Test
    fun selectPoiMarkerNullTest() {
        Mockito.`when`(mapInteractor.observeLocationFlowable())
            .thenReturn(Flowable.just(getHelsinkiLatLng()))
        Mockito.`when`(mapInteractor.observeCityActivitiesFlowable())
            .thenReturn(Flowable.just(getCityActivityList()))
        mapViewModel = MapViewModel(mapInteractor, mainRouter).apply {
            detailsButtonVisibilityEvent.observeForever(detailsButtonVisibilityObserver)
        }
        mapViewModel.selectPoiMarker(null)
        Mockito.verify(detailsButtonVisibilityObserver, Mockito.times(2)).onChanged(false)
    }

    private fun getHelsinkiLatLng() = LatLng(60.192059, 24.945831)

    private fun getCityActivityList() = listOf(
        getActivityDto()
    )

    private fun getActivityDto() = ActivityDto(
        id = "5e4beb78-f931-4a80-902f-f7ee7072b804",
        name = "Päivämatka Lohjalle",
        infoUrl = "https://www.virvehaahti.com/daytrip-to-lohja/",
        location = ActivityLocationDto(
            lat = 60.17026901245117,
            lon = 24.938474655151367,
            locality = "Helsinki",
            postalCode = "00100",
            streetAddress = "Helsinki"
        ),
        description = "description",
        imageLinkList = listOf()
    )

    companion object {
        private const val EXISTING_CITY_ACTIVITY_ID = "5e4beb78-f931-4a80-902f-f7ee7072b804"
    }
}
