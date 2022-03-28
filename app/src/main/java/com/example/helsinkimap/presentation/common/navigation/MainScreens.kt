package com.example.helsinkimap.presentation.common.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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

    object AppSystemSettingsScreen : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse(PACKAGE_CONST + context.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
    }

    private const val PACKAGE_CONST = "package:"
}
