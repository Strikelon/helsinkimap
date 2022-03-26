package com.example.helsinkimap.presentation

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.example.helsinkimap.presentation.common.BaseFragmentNavigator

class MainNavigator(
    activity: AppCompatActivity,
    @IdRes containerId: Int
) : BaseFragmentNavigator(activity, containerId)
