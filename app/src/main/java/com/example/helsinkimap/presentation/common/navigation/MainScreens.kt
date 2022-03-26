package com.example.helsinkimap.presentation.common.navigation

import androidx.fragment.app.Fragment
import com.example.helsinkimap.presentation.details.DetailsMvvmFragment
import com.example.helsinkimap.presentation.map.MapMvvmFragment
import com.example.helsinkimap.presentation.permission.PermissionMvvmFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

object MainScreens {
    object PermissionScreen : SupportAppScreen() {
        override fun getFragment(): Fragment = PermissionMvvmFragment.newInstance()
    }

    object MapScreen : SupportAppScreen() {
        override fun getFragment(): Fragment = MapMvvmFragment.newInstance()
    }

    object DetailsScreen : SupportAppScreen() {
        override fun getFragment(): Fragment = DetailsMvvmFragment.newInstance()
    }
}
