package com.example.helsinkimap.di.module.component

import com.example.helsinkimap.di.module.ActivityModule
import com.example.helsinkimap.di.module.viewmodel.ViewModelBuilderModule
import com.example.helsinkimap.di.module.viewmodel.ViewModelStorageModule
import com.example.helsinkimap.di.scope.ActivityScope
import com.example.helsinkimap.presentation.details.DetailsMvvmFragment
import com.example.helsinkimap.presentation.map.MapMvvmFragment
import com.example.helsinkimap.presentation.permission.PermissionMvvmFragment
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        ActivityModule::class,
        ViewModelStorageModule::class,
        ViewModelBuilderModule::class,
    ]
)
interface ActivityComponent {
    fun inject(fragment: PermissionMvvmFragment)
    fun inject(fragment: MapMvvmFragment)
    fun inject(fragment: DetailsMvvmFragment)
}
