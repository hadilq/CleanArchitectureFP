package com.github.hadilq.presentationcommon

import android.content.Intent

interface IntentFactory {

    fun create(page: Page): Intent

    enum class Page(
        val ID: Int
    ) {
        ARTISTS(0), ALBUMS(1), ALBUM_DETAILS(2)
    }

    enum class BundleKey(
        val KEY: String
    ) {
        ALBUMS_ARTIST_ID("ALBUMS_ARTIST_ID"),
        ALBUM_DETAILS_ALBUM_ID("ALBUM_DETAILS_ALBUM_ID")
    }
}