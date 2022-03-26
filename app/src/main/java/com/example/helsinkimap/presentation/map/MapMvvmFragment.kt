package com.example.helsinkimap.presentation.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.helsinkimap.databinding.FragmentMapBinding
import com.example.helsinkimap.presentation.arch.BaseMvvmFragment

class MapMvvmFragment : BaseMvvmFragment() {

    override val viewModel: MapViewModel by viewModels()
    private val binding by viewBinding(FragmentMapBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityComponent().inject(this)
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            title.setOnClickListener {
                viewModel.openDetailsScreen()
            }
        }
    }

    override fun observeLiveData() {
    }

    companion object {
        fun newInstance() = MapMvvmFragment()
    }
}
