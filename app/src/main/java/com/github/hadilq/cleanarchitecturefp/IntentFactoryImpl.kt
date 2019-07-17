package com.github.hadilq.cleanarchitecturefp

import android.content.Context
import android.content.Intent
import com.github.hadilq.presentationalbumdetails.AlbumDetailsActivity
import com.github.hadilq.presentationalbums.AlbumsActivity
import com.github.hadilq.presentationartists.ArtistsActivity
import com.github.hadilq.presentationcommon.IntentFactory
import javax.inject.Inject

class IntentFactoryImpl @Inject constructor(
    private val context: Context
) : IntentFactory {

    override fun create(page: IntentFactory.Page): Intent =
        when (page) {
            IntentFactory.Page.ARTISTS -> Intent(context, ArtistsActivity::class.java)
            IntentFactory.Page.ALBUMS -> Intent(context, AlbumsActivity::class.java)
            IntentFactory.Page.ALBUM_DETAILS -> Intent(context, AlbumDetailsActivity::class.java)
        }
}