package com.example.helsinkimap.presentation.map

import com.example.helsinkimap.domain.navigation.MainRouter
import com.example.helsinkimap.presentation.arch.viewmodel.MvvmViewModel
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val mainRouter: MainRouter
) : MvvmViewModel() {

    fun openDetailsScreen() {
        mainRouter.openDetailsScreen()
    }

    fun handleMapClick(latLng: LatLng) {
    }
}
