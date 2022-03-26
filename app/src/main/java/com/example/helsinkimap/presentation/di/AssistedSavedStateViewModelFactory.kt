package com.example.helsinkimap.presentation.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * General factory for ViewModels with SavedStateHandle.
 * This factory allows having all ViewModels in one InjectingSavedStateViewModelFactory
 *
 * Use With @AssistedFactory:
 *
 * ```
 * @AssistedFactory
 * interface Factory: AssistedSavedStateViewModelFactory<SomeViewModel>
 * ```
 * In your module add binding of AssistedFactory to this interface:
 * ```
 * @Binds
 * @IntoMap
 * @ViewModelKey(SomeViewModel::class)
 * abstract fun bindSomeViewModelFactory(factory: SomeViewModel.Factory) : AssistedSavedStateViewModelFactory<out ViewModel>
 * ```
 *
 * @see dagger.assisted.AssistedFactory
 */
interface AssistedSavedStateViewModelFactory<T : ViewModel> {
    fun create(savedStateHandle: SavedStateHandle): T
}
