package com.example.helsinkimap.presentation.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.helsinkimap.databinding.FragmentDetailsBinding
import com.example.helsinkimap.presentation.arch.BaseMvvmFragment

class DetailsMvvmFragment : BaseMvvmFragment() {

    override val viewModel: DetailsViewModel by viewModels()
    private val binding by viewBinding(FragmentDetailsBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityComponent().inject(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun observeLiveData() {
    }

    companion object {
        fun newInstance() = DetailsMvvmFragment()
    }
}
