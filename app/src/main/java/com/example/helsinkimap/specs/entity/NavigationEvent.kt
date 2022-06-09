package com.example.helsinkimap.specs.entity

sealed class NavigationEvent {
    object OpenMapScreen: NavigationEvent()
    class OpenDetailsScreen(val cityActivityDto: ActivityDto): NavigationEvent()
    object OpenAppSystemSettingsScreen: NavigationEvent()
    object Exit: NavigationEvent()
}
