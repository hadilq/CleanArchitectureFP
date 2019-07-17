package com.github.hadilq.presentationartists

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hadilq.cleanarchitecturefp.domain.entity.Artist
import com.github.hadilq.presentationcommon.Action
import com.github.hadilq.presentationcommon.inflate
import com.github.hadilq.presentationcommon.loadFromUrl
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import kotlinx.android.synthetic.main.artist.view.*

class ArtistViewHolder(
    parent: ViewGroup,
    actionStream: (Observable<Action>) -> Unit,
    private val picasso: Picasso
) : RecyclerView.ViewHolder(parent.inflate(R.layout.artist)) {

    private var artist: Artist? = null

    init {
        actionStream(RxView.clicks(itemView)
            .filter { artist != null }
            .map { artist?.let { a -> ArtistClickAction(a) } })
    }

    fun bindTo(a: Artist) {
        artist = a
        itemView.nameView.text = a.name
        itemView.pictureView.loadFromUrl(picasso, a.pictureBig)
    }
}