package com.example.helsinkimap.specs.uistate

import com.example.helsinkimap.specs.entity.NavigationEvent

data class PermissionUIState(
    val viewGroupVisibility: Boolean = false,
    val buttonsEnable: Boolean = false,
    val isProgress: Boolean = false,
    val needPermissionDialog: Boolean = false,
    val requestForegroundPermission: Boolean = false,
    val checkPermissions: Boolean = false,
    val navigation: NavigationEvent? = null
)
