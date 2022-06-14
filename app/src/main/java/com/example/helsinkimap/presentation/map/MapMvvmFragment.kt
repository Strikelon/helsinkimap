package com.example.helsinkimap.presentation.map

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.helsinkimap.R
import com.example.helsinkimap.core.ext.setVisible
import com.example.helsinkimap.databinding.FragmentMapBinding
import com.example.helsinkimap.presentation.arch.BaseMvvmFragment
import com.example.helsinkimap.specs.entity.ActivityDto
import com.example.helsinkimap.specs.entity.ErrorTypes
import com.example.helsinkimap.specs.entity.NavigationEvent
import com.example.helsinkimap.specs.uistate.MapUIState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapMvvmFragment : BaseMvvmFragment(), OnMapReadyCallback {

    override val viewModel: MapViewModel by viewModels()
    private val binding by viewBinding(FragmentMapBinding::inflate)

    private var mMap: GoogleMap? = null
    private var currentPositionMarker: Marker? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        with(binding) {
            detailsButton.setOnClickListener {
                viewModel.openDetailsScreen()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { mapUIState: MapUIState ->
                    mapUIState.currentUserPosition?.let { currentUserPositionNotNull: LatLng ->
                        handleCurrentUserPosition(currentUserPositionNotNull)
                    }
                    handlePoiListLiveData(mapUIState.poiList)
                    handleDetailsButtonVisibility(mapUIState.detailsButtonVisibility)
                    mapUIState.error?.let {
                        handleErrorEvent(it)
                    }
                    mapUIState.navigation?.let {
                        handleNavigationEvent(it)
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap.apply {
            setOnMarkerClickListener {
                viewModel.selectPoiMarker(it.tag as? String)
                false
            }
            setOnMapClickListener {
                viewModel.skipSelectedCityActivity()
            }
        }
    }

    private fun handleCurrentUserPosition(latLng: LatLng) {
        mMap?.let {
            mMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng,
                    MAP_ANIMATE_APPROXIMATION_VALUE
                )
            )
            updateCurrentPositionMarker(latLng)
        }
    }

    private fun updateCurrentPositionMarker(latLng: LatLng) {
        mMap?.let {
            currentPositionMarker?.remove()
            addCurrentPositionMarker(latLng)
        }
    }

    private fun addCurrentPositionMarker(latLng: LatLng) {
        currentPositionMarker = mMap?.addMarker(MarkerOptions().position(latLng))?.apply {
            setIcon(BitmapDescriptorFactory.fromAsset(CURRENT_POSITION_ICON))
        }
    }

    private fun handlePoiListLiveData(poiList: List<ActivityDto>) {
        removeAllPoiMarkers()
        poiList.forEach { cityActivityDto: ActivityDto ->
            val id = cityActivityDto.id
            val lat = cityActivityDto.location.lat
            val lng = cityActivityDto.location.lon
            val poiName = cityActivityDto.name
            showPoiMarker(id, LatLng(lat, lng), poiName)
        }
    }

    private fun showPoiMarker(id: String, latLng: LatLng, title: String) {
        mMap?.addMarker(MarkerOptions().position(latLng))?.apply {
            tag = id
            setTitle(title)
            setIcon(BitmapDescriptorFactory.fromAsset(CUSTOM_MARKER_ICON))
        }
    }

    private fun removeAllPoiMarkers() {
        val currentPositionLatLng = currentPositionMarker?.position
        mMap?.clear()
        currentPositionLatLng?.let {
            addCurrentPositionMarker(it)
        }
    }

    private fun handleDetailsButtonVisibility(isVisible: Boolean) {
        binding.detailsButton.setVisible(isVisible)
    }

    private fun handleErrorEvent(errorTypes: ErrorTypes) {
        when (errorTypes) {
            ErrorTypes.NETWORK_ERROR -> {
                showNetworkErrorDialog()
            }
            ErrorTypes.GPS_USE_ERROR -> {
                showGpsUseErrorDialog()
            }
            ErrorTypes.PERMISSION_ERROR -> {
                showPermissionErrorDialog()
            }
            ErrorTypes.GPS_ENABLE_ERROR -> {
                showGpsEnableErrorDialog()
            }
            else -> {
                // nothing to do
            }
        }
        viewModel.errorHandled()
    }

    private fun showNetworkErrorDialog() {
        ErrorDialogFragment(
            title = getString(R.string.error),
            message = getString(R.string.network_error),
            positiveButtonName = getString(R.string.ok)
        ).show(childFragmentManager, ERROR_DIALOG_FRAGMENT_TAG)
    }

    private fun showGpsUseErrorDialog() {
        ErrorDialogFragment(
            title = getString(R.string.error),
            message = getString(R.string.gps_use_error),
            positiveButtonName = getString(R.string.ok)
        ).show(childFragmentManager, ERROR_DIALOG_FRAGMENT_TAG)
    }

    private fun showPermissionErrorDialog() {
        ErrorDialogFragment(
            title = getString(R.string.error),
            message = getString(R.string.app_needs_permission),
            positiveButtonName = getString(R.string.ok)
        ).show(childFragmentManager, ERROR_DIALOG_FRAGMENT_TAG)
    }

    private fun showGpsEnableErrorDialog() {
        ErrorDialogFragment(
            title = getString(R.string.error),
            message = getString(R.string.gps_disabled),
            positiveButtonName = getString(R.string.ok)
        ).show(childFragmentManager, ERROR_DIALOG_FRAGMENT_TAG)
    }

    private fun handleNavigationEvent(navigationEvent: NavigationEvent) {
        when (navigationEvent) {
            is NavigationEvent.OpenDetailsScreen -> {
                viewModel.navigationEventHandled()
                val direction = MapMvvmFragmentDirections.actionMapMvvmFragmentToDetailsMvvmFragment(navigationEvent.cityActivityDto)
                findNavController().navigate(direction)
            }
            else -> {
                // nothing to do
            }
        }
    }

    class ErrorDialogFragment(
        val title: String,
        val message: String,
        val positiveButtonName: String,
        val positiveAction: () -> (Unit) = {},
    ) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonName) { _, _ ->
                    positiveAction.invoke()
                }
                .create()
    }

    companion object {
        private const val CURRENT_POSITION_ICON = "current_position.png"
        private const val CUSTOM_MARKER_ICON = "custom_marker.png"
        private const val MAP_ANIMATE_APPROXIMATION_VALUE = 17f
        private const val ERROR_DIALOG_FRAGMENT_TAG = "error_dialog_fragment_tag"
    }
}
