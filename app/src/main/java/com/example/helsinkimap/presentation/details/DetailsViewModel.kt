package com.example.helsinkimap.presentation.details

import com.example.helsinkimap.domain.navigation.MainRouter
import com.example.helsinkimap.presentation.arch.viewmodel.MvvmViewModel
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val mainRouter: MainRouter
) : MvvmViewModel()
