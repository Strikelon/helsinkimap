package com.example.helsinkimap.presentation.permission

import com.example.helsinkimap.presentation.arch.viewmodel.MvvmViewModel
import com.example.helsinkimap.specs.entity.NavigationEvent
import com.example.helsinkimap.specs.uistate.PermissionUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor() : MvvmViewModel() {

    private var permissionsGranted = false
    private var proceed = false

    private val _uiState = MutableStateFlow(PermissionUIState())
    val uiState: StateFlow<PermissionUIState> = _uiState

    init {
        proceedIfPossible()
    }

    fun permissionsGranted() {
        permissionsGranted = true
        proceedIfPossible()
    }

    private fun proceedIfPossible() {
        if (permissionsGranted && !proceed) {
            proceed = true
            _uiState.update { currentState: PermissionUIState ->
                currentState.copy(
                    checkPermissions = false,
                    requestForegroundPermission = false,
                    navigation = NavigationEvent.OpenMapScreen
                )
            }
        } else {
            _uiState.update { currentState: PermissionUIState ->
                currentState.copy(
                    checkPermissions = true,
                    isProgress = true
                )
            }
        }
    }

    fun requestAcceptPermissions() {
        _uiState.update { currentState: PermissionUIState ->
            currentState.copy(
                isProgress = true,
                buttonsEnable = false,
                requestForegroundPermission = true
            )
        }
    }

    fun nonGrantedPermissions() {
        _uiState.update { currentState: PermissionUIState ->
            currentState.copy(
                viewGroupVisibility = true,
                isProgress = false,
                checkPermissions = false,
                requestForegroundPermission = false,
                buttonsEnable = true
            )
        }
    }

    fun permissionsDenied() {
        _uiState.update { currentState: PermissionUIState ->
            currentState.copy(
                isProgress = false,
                buttonsEnable = true,
                checkPermissions = false,
                requestForegroundPermission = false
            )
        }
    }

    fun neverAskAgain() {
        _uiState.update { currentState: PermissionUIState ->
            currentState.copy(
                needPermissionDialog = true,
                checkPermissions = false,
                requestForegroundPermission = false
            )
        }
    }

    fun permissionSettingsClick() {
        _uiState.update { currentState: PermissionUIState ->
            currentState.copy(
                navigation = NavigationEvent.OpenAppSystemSettingsScreen
            )
        }
    }

    fun exit() {
        _uiState.update { currentState: PermissionUIState ->
            currentState.copy(
                navigation = NavigationEvent.Exit
            )
        }
    }

    fun needPermissionDialogShown() {
        _uiState.update { currentState: PermissionUIState ->
            currentState.copy(
                needPermissionDialog = false
            )
        }
    }

    fun navigationEventHandled() {
        _uiState.update { currentState: PermissionUIState ->
            currentState.copy(
                navigation = null
            )
        }
    }
}
