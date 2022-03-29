package com.example.helsinkimap.core.ext

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.BaseRequestOptions

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

fun String.toHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

fun ImageView.loadImage(url: String?, requestOptions: BaseRequestOptions<*>) = Glide.with(context)
    .load(url)
    .apply(requestOptions)
    .into(this)
