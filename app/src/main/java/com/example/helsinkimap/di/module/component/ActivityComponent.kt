package com.example.helsinkimap.di.module.component

import com.example.helsinkimap.di.module.ActivityModule
import com.example.helsinkimap.di.module.viewmodel.ViewModelBuilderModule
import com.example.helsinkimap.di.module.viewmodel.ViewModelStorageModule
import com.example.helsinkimap.di.scope.ActivityScope
import dagger.Subcomponent

@ActivityScope
@Subcomponent(
    modules = [
        ActivityModule::class,
        ViewModelStorageModule::class,
        ViewModelBuilderModule::class,
    ]
)
interface ActivityComponent
