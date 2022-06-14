package com.example.helsinkimap.core.navigation

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment

fun Fragment.exitApp() {
    requireActivity().finish()
}

fun Fragment.openApplicationDetailsSettings() {
    startActivity(
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            data = Uri.parse(PACKAGE_CONST + requireContext().packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
    )
}

private const val PACKAGE_CONST = "package:"
