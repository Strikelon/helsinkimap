package com.example.helsinkimap.presentation.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.helsinkimap.R
import com.example.helsinkimap.databinding.FragmentMapBinding
import com.example.helsinkimap.presentation.arch.BaseMvvmFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapMvvmFragment : BaseMvvmFragment(), OnMapReadyCallback {

    override val viewModel: MapViewModel by viewModels()
    private val binding by viewBinding(FragmentMapBinding::inflate)

    private var mMap: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityComponent().inject(this)
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
        with(binding) {
            title.setOnClickListener {
                viewModel.openDetailsScreen()
            }
        }
    }

    override fun observeLiveData() {
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap.apply {
            setOnMapClickListener {
                viewModel.handleMapClick(it)
            }
        }
    }

    companion object {
        fun newInstance() = MapMvvmFragment()
    }
}
