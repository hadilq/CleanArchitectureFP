package com.github.hadilq.presentationcommon

import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import com.github.hadilq.cleanarchitecturefp.domain.entity.Album

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
        ALBUM_DETAILS_ALBUM("ALBUM_DETAILS_ALBUM")
    }

    data class AlbumCarrier(
        val id: String,
        val title: String,
        val coverBig: String
    ) : Parcelable {

        fun toAlbum(): Album = Album(
            id = id,
            title = title,
            coverBig = coverBig,
            cover = "",
            coverSmall = "",
            coverMedium = "",
            coverXl = ""
        )

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeString(id)
            writeString(title)
            writeString(coverBig)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<AlbumCarrier> = object : Parcelable.Creator<AlbumCarrier> {
                override fun createFromParcel(source: Parcel): AlbumCarrier = AlbumCarrier(source)
                override fun newArray(size: Int): Array<AlbumCarrier?> = arrayOfNulls(size)
            }

            fun Album.fromAlbum(): AlbumCarrier = AlbumCarrier(
                id = id,
                title = title,
                coverBig = coverBig
            )
        }
    }

}