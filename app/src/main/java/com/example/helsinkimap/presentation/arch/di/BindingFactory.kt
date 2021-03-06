package com.example.helsinkimap.presentation.arch.di

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

fun interface BindingFactory<Binding : ViewBinding> {
    fun inflate(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean,
    ): Binding
}
