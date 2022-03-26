package com.example.helsinkimap.di.module.component

import com.example.helsinkimap.di.module.ActivityModule
import com.example.helsinkimap.di.module.AppModule
import com.example.helsinkimap.di.module.NetworkModule
import com.example.helsinkimap.presentation.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class
    ]
)
interface ProjectComponent {

    fun activityComponent(module: ActivityModule): ActivityComponent

    fun inject(mainActivity: MainActivity)
}
