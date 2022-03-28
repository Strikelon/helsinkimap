package com.example.helsinkimap.domain.navigation

import com.example.helsinkimap.presentation.common.navigation.MainScreens
import com.example.helsinkimap.specs.entity.ActivityDto
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

class MainRouter(cicerone: Cicerone<Router>) : BaseRouter(cicerone) {
    fun openPermissionScreen() = router.newRootScreen(MainScreens.PermissionScreen)
    fun openMapScreen() = router.newRootScreen(MainScreens.MapScreen)
    fun openDetailsScreen(cityActivityDto: ActivityDto) = router.navigateTo(MainScreens.DetailsScreen(cityActivityDto))
    fun openAppSystemSettingsScreen() = router.navigateTo(MainScreens.AppSystemSettingsScreen)
}
