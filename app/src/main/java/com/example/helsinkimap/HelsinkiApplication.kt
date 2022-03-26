package com.example.helsinkimap

import androidx.multidex.MultiDexApplication
import com.example.helsinkimap.di.module.Injector

class HelsinkiApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        Injector.initialize(this)
    }
}
