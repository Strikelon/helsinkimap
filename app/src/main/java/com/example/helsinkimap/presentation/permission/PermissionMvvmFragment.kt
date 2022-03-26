package com.example.helsinkimap.presentation.permission

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.helsinkimap.databinding.FragmentPermissionsBinding
import com.example.helsinkimap.presentation.arch.BaseMvvmFragment

class PermissionMvvmFragment : BaseMvvmFragment() {

    override val viewModel: PermissionViewModel by viewModels()
    private val binding by viewBinding(FragmentPermissionsBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityComponent().inject(this)
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            title.setOnClickListener {
                viewModel.openMapScreen()
            }
        }
    }

    override fun observeLiveData() {
    }

    companion object {
        fun newInstance() = PermissionMvvmFragment()
    }
}
