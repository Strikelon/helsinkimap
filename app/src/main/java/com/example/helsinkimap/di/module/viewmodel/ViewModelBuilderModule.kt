package com.example.helsinkimap.di.module.viewmodel

import androidx.lifecycle.ViewModel
import com.example.helsinkimap.di.qualifier.ViewModelKey
import com.example.helsinkimap.presentation.details.DetailsViewModel
import com.example.helsinkimap.presentation.map.MapViewModel
import com.example.helsinkimap.presentation.permission.PermissionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelBuilderModule {

    @Binds
    @IntoMap
    @ViewModelKey(PermissionViewModel::class)
    fun bindsPermissionViewModel(vm: PermissionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    fun bindsMapViewModel(vm: MapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    fun bindsDetailsViewModel(vm: DetailsViewModel): ViewModel
}
