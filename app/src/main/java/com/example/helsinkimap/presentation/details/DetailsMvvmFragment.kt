package com.example.helsinkimap.presentation.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.helsinkimap.core.ext.toHtml
import com.example.helsinkimap.databinding.FragmentDetailsBinding
import com.example.helsinkimap.presentation.arch.BaseMvvmFragment
import com.example.helsinkimap.presentation.arch.delegate.FragmentArgumentDelegate
import com.example.helsinkimap.specs.entity.ActivityDto

class DetailsMvvmFragment : BaseMvvmFragment() {

    override val viewModel: DetailsViewModel by viewModels()
    private val binding by viewBinding(FragmentDetailsBinding::inflate)

    private var cityActivityDto: ActivityDto? by FragmentArgumentDelegate(
        ARGUMENT_SELECTED_CITY_ACTIVITY
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityComponent().inject(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun observeLiveData() {
        with(viewModel) {
            poiEvent.observe(viewLifecycleOwner) { showPoiDetails(it) }
        }
    }

    private fun showPoiDetails(activityDto: ActivityDto) {
        with(binding) {
            poiTitle.text = activityDto.name
            val locality = activityDto.location.locality
            val postalCode = activityDto.location.postalCode
            val streetAddress = activityDto.location.streetAddress
            val address = "$locality, $postalCode, $streetAddress"
            poiAddress.text = address
            poiLink.text = activityDto.infoUrl
            poiDescription.text = activityDto.description.toHtml()
        }
    }

    companion object {
        const val ARGUMENT_SELECTED_CITY_ACTIVITY = "argument_selected_city_activity"

        fun newInstance(
            cityActivityDto: ActivityDto
        ): DetailsMvvmFragment =
            DetailsMvvmFragment().apply {
                this.cityActivityDto = cityActivityDto
            }
    }
}
