package com.example.helsinkimap.presentation.permission

import com.example.helsinkimap.domain.navigation.MainRouter
import com.example.helsinkimap.presentation.arch.viewmodel.MvvmViewModel
import javax.inject.Inject

class PermissionViewModel @Inject constructor(
    private val mainRouter: MainRouter
) : MvvmViewModel() {

    fun openMapScreen() {
        mainRouter.openMapScreen()
    }
}
