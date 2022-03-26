package com.example.helsinkimap.presentation.common

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import ru.terrakok.cicerone.android.support.SupportAppNavigator

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseFragmentNavigator(
    val activity: AppCompatActivity,
    @IdRes val containerId: Int
) : SupportAppNavigator(activity, containerId)
