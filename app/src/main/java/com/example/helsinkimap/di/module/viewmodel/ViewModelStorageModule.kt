package com.example.helsinkimap.di.module.viewmodel

import androidx.lifecycle.ViewModel
import com.example.helsinkimap.presentation.di.AssistedSavedStateViewModelFactory
import dagger.Module
import dagger.multibindings.Multibinds

@Suppress("unused")
@Module
abstract class ViewModelStorageModule {

    @Multibinds
    abstract fun viewModels(): Map<Class<out ViewModel>, @JvmSuppressWildcards ViewModel>

    @Multibinds
    abstract fun assistedViewModels(): Map<Class<out ViewModel>, @JvmSuppressWildcards AssistedSavedStateViewModelFactory<out ViewModel>>
}
