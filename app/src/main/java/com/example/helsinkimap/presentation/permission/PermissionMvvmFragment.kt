package com.example.helsinkimap.presentation.permission

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.helsinkimap.R
import com.example.helsinkimap.core.ext.setVisible
import com.example.helsinkimap.databinding.FragmentPermissionsBinding
import com.example.helsinkimap.presentation.arch.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionMvvmFragment : BaseMvvmFragment() {

    override val viewModel: PermissionViewModel by viewModels()
    private val binding by viewBinding(FragmentPermissionsBinding::inflate)

    private val permRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                viewModel.permissionsGranted()
            } else {
                viewModel.permissionsDenied()
                val neverAskAgain =
                    !checkShouldShowRequestPermissionsRationale(permissions.keys.toList())
                if (neverAskAgain) {
                    viewModel.neverAskAgain()
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            refuseButton.setOnClickListener {
                viewModel.exit()
            }
            acceptButton.setOnClickListener {
                viewModel.requestAcceptPermissions()
            }
        }
    }

    override fun observeLiveData() {
        with(viewModel) {
            loadingProgressState.observe(viewLifecycleOwner) { showProgress(it) }
            viewGroupVisibilityState.observe(viewLifecycleOwner) { setViewGroupVisibility(it) }
            needPermissionDialogEvent.observe(viewLifecycleOwner) { showNeedPermissionDialog() }
            requestForegroundPermissionsEvent.observe(viewLifecycleOwner) { requestForegroundPermissions() }
            buttonsEnabledState.observe(viewLifecycleOwner) { setButtonsEnabled(it) }
            checkPermissionsEvent.observe(viewLifecycleOwner) { checkPermissions() }
        }
    }

    private fun checkPermissions() {
        if (getNotGrantedForegroundLocationPermissionList().isEmpty()) {
            viewModel.permissionsGranted()
        } else {
            viewModel.nonGrantedPermissions()
        }
    }

    private fun showProgress(inProgress: Boolean) {
        with(binding) {
            loginProgressMask.setVisible(inProgress)
            loginProgressBar.setVisible(inProgress)
        }
    }

    private fun setViewGroupVisibility(visible: Boolean) {
        binding.permissionViewGroup.setVisible(visible)
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        with(binding) {
            refuseButton.isEnabled = enabled
            acceptButton.isEnabled = enabled
        }
    }

    private fun requestForegroundPermissions() {
        val foregroundLocationPermissionsToRequest = getNotGrantedForegroundLocationPermissionList()
        if (foregroundLocationPermissionsToRequest.isEmpty()) {
            viewModel.permissionsGranted()
        } else {
            permRequestLauncher.launch(foregroundLocationPermissionsToRequest.toTypedArray())
        }
    }

    private fun getNotGrantedForegroundLocationPermissionList(): List<String> {
        val permissions: MutableList<String> = ArrayList<String>().apply {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        val permissionsToRequest: MutableList<String> = ArrayList()
        permissions.forEach { permission ->
            try {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsToRequest.add(permission)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e("Error", "$permission is not a correct permission name! $e")
            }
        }
        return permissionsToRequest
    }

    private fun checkShouldShowRequestPermissionsRationale(permissionsList: List<String>): Boolean {
        var shouldShow = true
        permissionsList.forEach { permission ->
            shouldShow = shouldShow && shouldShowRequestPermissionRationale(permission)
        }
        return shouldShow
    }

    private fun showNeedPermissionDialog() {
        NeedPermissionDialogFragment(
            positiveAction = {
                viewModel.permissionSettingsClick()
            }
        ).show(childFragmentManager, PERMISSION_DIALOG_FRAGMENT_TAG)
    }

    class NeedPermissionDialogFragment(
        val positiveAction: () -> (Unit)
    ) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.permission_app_use_location))
                .setPositiveButton(getString(R.string.permission_settings)) { _, _ ->
                    positiveAction.invoke()
                }
                .setNegativeButton(getString(R.string.permission_deny)) { _, _ -> }
                .create()
    }

    companion object {
        private const val PERMISSION_DIALOG_FRAGMENT_TAG = "permission_dialog_fragment_tag"

        fun newInstance() = PermissionMvvmFragment()
    }
}
