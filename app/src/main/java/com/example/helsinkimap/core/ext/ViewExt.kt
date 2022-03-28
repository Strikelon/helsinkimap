package com.example.helsinkimap.core.ext

import android.view.View

val View.isVisible get() = visibility == View.VISIBLE

val View.isGone get() = visibility == View.GONE

val View.isInvisible get() = visibility == View.INVISIBLE

fun View.setVisible(isVisible: Boolean) {
    if (isVisible) this.show() else this.hide()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.showIf(visible: Boolean) {
    if (visible) show() else hide()
}

fun View.dissolve() {
    visibility = View.INVISIBLE
}

fun View.dissolveIf(invisible: Boolean) {
    if (invisible) dissolve() else show()
}

fun View.hide() {
    visibility = View.GONE
}

/**
 * publish View.visibility to View.VISIBLE
 */
fun showViews(vararg views: View?) = views.forEach { it?.show() }

/**
 * publish View.visibility to View.INVISIBLE
 */
fun dissolveViews(vararg views: View?) = views.forEach { it?.dissolve() }

/**
 * publish View.visibility to View.GONE
 */
fun hideViews(vararg views: View?) = views.forEach { it?.hide() }
