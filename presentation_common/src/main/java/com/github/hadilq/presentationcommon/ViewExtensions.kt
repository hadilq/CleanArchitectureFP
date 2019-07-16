package com.github.hadilq.presentationcommon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.squareup.picasso.Picasso

fun View.isVisible() = visibility == View.VISIBLE

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun ImageView.loadFromUrl(picasso: Picasso, url: String) {
    picasso.cancelRequest(this)
    setImageDrawable(null)
    picasso.load(url).into(this)
}
