package com.example.helsinkimap.di.module

import com.example.helsinkimap.HelsinkiApplication
import com.example.helsinkimap.di.module.component.DaggerProjectComponent
import com.example.helsinkimap.di.module.component.ProjectComponent
import java.util.concurrent.atomic.AtomicBoolean

object Injector {
    private val INITIALIZED = AtomicBoolean(false)

    lateinit var projectComponent: ProjectComponent
        private set

    fun initialize(application: HelsinkiApplication) {
        if (INITIALIZED.compareAndSet(false, true)) {
            projectComponent = DaggerProjectComponent.builder()
                .appModule(AppModule(application))
                .build()
        }
    }
}
