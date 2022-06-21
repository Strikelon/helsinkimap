package com.example.helsinkimap.presentation.map

import com.example.helsinkimap.model.Models
import com.example.helsinkimap.specs.entity.ActivityDto
import com.example.helsinkimap.specs.entity.ErrorTypes
import com.example.helsinkimap.specs.entity.NavigationEvent
import com.example.helsinkimap.usecase.FakeObserveCityActivitiesUseCase
import com.example.helsinkimap.usecase.FakeObserveGpsErrorUseCase
import com.example.helsinkimap.usecase.FakeObserveLocationUseCase
import com.example.helsinkimap.util.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MapViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Test
    fun `observe location success`() {
        val viewModel = MapViewModel(
            FakeObserveCityActivitiesUseCase(),
            FakeObserveGpsErrorUseCase(),
            FakeObserveLocationUseCase()
        )
        viewModel.attach()
        coroutineRule.testDispatcher.scheduler.runCurrent()
        val actualState = viewModel.uiState.value
        assertEquals(Models.HELSINKI_LAT_LNG, actualState.currentUserPosition)
    }

    @Test
    fun `observe location error`() {
        val viewModel = MapViewModel(
            FakeObserveCityActivitiesUseCase(),
            FakeObserveGpsErrorUseCase(),
            FakeObserveLocationUseCase(status = FakeObserveLocationUseCase.FakeObserveLocationUseCaseStatus.ERROR_LOCATION_DENY)
        )
        viewModel.attach()
        coroutineRule.testDispatcher.scheduler.runCurrent()
        val actualState = viewModel.uiState.value
        assertEquals(null, actualState.currentUserPosition)
    }

    @Test
    fun `observe city activities success`() {
        val viewModel = MapViewModel(
            FakeObserveCityActivitiesUseCase(),
            FakeObserveGpsErrorUseCase(),
            FakeObserveLocationUseCase()
        )
        viewModel.attach()
        coroutineRule.testDispatcher.scheduler.runCurrent()
        val actualState = viewModel.uiState.value
        assertEquals(Models.listActivityDto, actualState.poiList)
    }

    @Test
    fun `observe city activities error`() {
        val viewModel = MapViewModel(
            FakeObserveCityActivitiesUseCase(null),
            FakeObserveGpsErrorUseCase(),
            FakeObserveLocationUseCase()
        )
        viewModel.attach()
        coroutineRule.testDispatcher.scheduler.runCurrent()
        val actualState = viewModel.uiState.value
        assertEquals(listOf<ActivityDto>(), actualState.poiList)
    }

    @Test
    fun `observe gps error none`() {
        val viewModel = MapViewModel(
            FakeObserveCityActivitiesUseCase(),
            FakeObserveGpsErrorUseCase(),
            FakeObserveLocationUseCase()
        )
        viewModel.attach()
        coroutineRule.testDispatcher.scheduler.runCurrent()
        val actualState = viewModel.uiState.value
        assertEquals(ErrorTypes.NONE, actualState.error)
    }

    @Test
    fun `observe gps use error handled`() {
        val viewModel = MapViewModel(
            FakeObserveCityActivitiesUseCase(),
            FakeObserveGpsErrorUseCase(ErrorTypes.GPS_USE_ERROR),
            FakeObserveLocationUseCase()
        )
        viewModel.attach()
        coroutineRule.testDispatcher.scheduler.runCurrent()
        val actualState = viewModel.uiState.value
        assertEquals(ErrorTypes.GPS_USE_ERROR, actualState.error)
        viewModel.errorHandled()
        val actualState2 = viewModel.uiState.value
        assertEquals(ErrorTypes.NONE, actualState2.error)
    }

    @Test
    fun `select poi markers`() {
        val viewModel = MapViewModel(
            FakeObserveCityActivitiesUseCase(),
            FakeObserveGpsErrorUseCase(),
            FakeObserveLocationUseCase()
        )
        viewModel.attach()
        coroutineRule.testDispatcher.scheduler.runCurrent()
        viewModel.selectPoiMarker(Models.listActivityDto[0].id)
        val actualState = viewModel.uiState.value
        assertTrue(actualState.detailsButtonVisibility)
    }

    @Test
    fun `open details screen`() {
        val viewModel = MapViewModel(
            FakeObserveCityActivitiesUseCase(),
            FakeObserveGpsErrorUseCase(),
            FakeObserveLocationUseCase()
        )
        viewModel.attach()
        coroutineRule.testDispatcher.scheduler.runCurrent()
        viewModel.selectPoiMarker(Models.listActivityDto[0].id)
        viewModel.openDetailsScreen()
        val actualState = viewModel.uiState.value
        assertTrue(actualState.navigation is NavigationEvent.OpenDetailsScreen)
    }

    @Test
    fun `skip selected city activity`() {
        val viewModel = MapViewModel(
            FakeObserveCityActivitiesUseCase(),
            FakeObserveGpsErrorUseCase(),
            FakeObserveLocationUseCase()
        )
        viewModel.attach()
        coroutineRule.testDispatcher.scheduler.runCurrent()
        viewModel.selectPoiMarker(Models.listActivityDto[0].id)
        val actualState = viewModel.uiState.value
        assertTrue(actualState.detailsButtonVisibility)
        viewModel.skipSelectedCityActivity()
        val actualState2 = viewModel.uiState.value
        assertTrue(!actualState2.detailsButtonVisibility)
    }
}
