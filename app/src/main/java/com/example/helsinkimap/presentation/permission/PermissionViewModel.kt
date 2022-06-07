package com.example.helsinkimap.presentation.permission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.helsinkimap.domain.navigation.MainRouter
import com.example.helsinkimap.presentation.arch.viewmodel.MvvmViewModel
import com.example.helsinkimap.presentation.arch.viewmodel.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val mainRouter: MainRouter
) : MvvmViewModel() {

    private var permissionsGranted = false
    private var proceed = false

    val viewGroupVisibilityState: LiveData<Boolean> by lazy { SingleLiveData() }
    val needPermissionDialogEvent: LiveData<Unit> by lazy { SingleLiveData() }
    val requestForegroundPermissionsEvent: LiveData<Unit> by lazy { SingleLiveData() }
    val buttonsEnabledState: LiveData<Boolean> by lazy { MutableLiveData() }
    val checkPermissionsEvent: LiveData<Unit> by lazy { SingleLiveData() }

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
            openMapScreen()
        } else {
            checkPermissionsEvent.postValue(Unit)
        }
    }

    private fun openMapScreen() {
        mainRouter.openMapScreen()
    }

    fun requestAcceptPermissions() {
        loadingProgressState.postValue(true)
        buttonsEnabledState.postValue(false)
        requestForegroundPermissionsEvent.postValue(Unit)
    }

    fun nonGrantedPermissions() {
        viewGroupVisibilityState.postValue(true)
        loadingProgressState.postValue(false)
    }

    fun permissionsDenied() {
        loadingProgressState.postValue(false)
        buttonsEnabledState.postValue(true)
    }

    fun neverAskAgain() {
        needPermissionDialogEvent.postValue(Unit)
    }

    fun permissionSettingsClick() {
        mainRouter.openAppSystemSettingsScreen()
    }

    fun exit() {
        mainRouter.exit()
    }
}
