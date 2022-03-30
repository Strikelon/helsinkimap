package com.example.helsinkimap.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.example.helsinkimap.presentation.details.DetailsMvvmFragment
import com.example.helsinkimap.presentation.details.DetailsViewModel
import com.example.helsinkimap.specs.entity.ActivityDto
import com.example.helsinkimap.specs.entity.ActivityLocationDto
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
class DetailsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var detailsViewModel: DetailsViewModel

    @Mock
    lateinit var savedStateHandle: SavedStateHandle

    @Mock
    lateinit var observer: Observer<ActivityDto>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.lenient()
            .`when`(savedStateHandle.get<ActivityDto>(DetailsMvvmFragment.ARGUMENT_SELECTED_CITY_ACTIVITY))
            .thenReturn(getActivityDto())
        detailsViewModel = DetailsViewModel(savedStateHandle)
        detailsViewModel.poiEvent.observeForever(observer)
    }

    @Test
    fun savedStateHandleNotNullTest() {
        Mockito.verify(observer).onChanged(getActivityDto())
    }

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
}
