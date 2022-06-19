package com.example.helsinkimap.core.ext

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.BaseRequestOptions

fun String.fromHtml(): Spanned {
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
