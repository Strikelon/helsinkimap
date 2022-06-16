package com.example.helsinkimap.presentation.arch.viewmodel

import androidx.lifecycle.ViewModel

@Suppress("MemberVisibilityCanBePrivate")
abstract class MvvmViewModel : ViewModel() {

    open fun attach() {
    }

    open fun detach() {
    }
}
