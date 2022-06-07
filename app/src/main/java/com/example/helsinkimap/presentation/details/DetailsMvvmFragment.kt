package com.example.helsinkimap.presentation.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helsinkimap.core.ext.setVisible
import com.example.helsinkimap.core.ext.toHtml
import com.example.helsinkimap.databinding.FragmentDetailsBinding
import com.example.helsinkimap.presentation.arch.BaseMvvmFragment
import com.example.helsinkimap.presentation.arch.delegate.FragmentArgumentDelegate
import com.example.helsinkimap.specs.entity.ActivityDto
import com.example.helsinkimap.specs.entity.ActivityImageLinkDto
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsMvvmFragment : BaseMvvmFragment() {

    override val viewModel: DetailsViewModel by viewModels()
    private val binding by viewBinding(FragmentDetailsBinding::inflate)

    private var cityActivityDto: ActivityDto? by FragmentArgumentDelegate(
        ARGUMENT_SELECTED_CITY_ACTIVITY
    )

    private val poiDetailsRecyclerViewAdapter: PoiDetailsRecyclerViewAdapter by lazy {
        PoiDetailsRecyclerViewAdapter(
            requireContext()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            poiDetailsRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                adapter = poiDetailsRecyclerViewAdapter
            }
        }
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
            showImages(activityDto.imageLinkList)
        }
    }

    private fun showImages(imageUrlList: List<ActivityImageLinkDto>) {
        with(binding) {
            if (imageUrlList.isEmpty()) {
                poiDetailsRecyclerView.setVisible(false)
            } else {
                poiDetailsRecyclerView.setVisible(true)
                poiDetailsRecyclerViewAdapter.setData(imageUrlList)
            }
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
