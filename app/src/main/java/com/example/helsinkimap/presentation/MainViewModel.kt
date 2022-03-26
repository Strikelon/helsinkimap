package com.example.helsinkimap.presentation

import androidx.lifecycle.ViewModel
import com.example.helsinkimap.domain.navigation.MainRouter
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val mainRouter: MainRouter
) : ViewModel() {

    init {
        mainRouter.openPermissionScreen()
    }

    fun back() {
        mainRouter.exit()
    }
}
