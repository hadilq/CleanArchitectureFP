package com.github.hadilq.presentationalbums

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.hadilq.cleanarchitecturefp.domain.entity.Album
import com.github.hadilq.presentationcommon.Action
import com.github.hadilq.presentationcommon.inflate
import com.github.hadilq.presentationcommon.loadFromUrl
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import kotlinx.android.synthetic.main.album.view.*

class AlbumViewHolder(
    parent: ViewGroup,
    actionStream: (Observable<Action>) -> Unit,
    private val picasso: Picasso,
    width: Int
) : RecyclerView.ViewHolder(parent.inflate(R.layout.album)) {

    private var album: Album? = null

    init {
        itemView.albumLayout.layoutParams.width = width
        itemView.albumLayout.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        itemView.albumLayout.layoutParams = itemView.albumLayout.layoutParams

        itemView.coverView.layoutParams.width =
            width - 3 * itemView.resources.getDimension(R.dimen.standard_half).toInt() / 4
        itemView.coverView.layoutParams.height = itemView.coverView.layoutParams.width
        itemView.coverView.layoutParams = itemView.coverView.layoutParams

        actionStream(
            RxView.clicks(itemView)
                .filter { album != null }
                .map { album?.let { a -> AlbumClickAction(a) } }
        )
    }

    fun bindTo(album: Album) {

        itemView.titleView.text = album.title
        album.artist?.let { a ->
            itemView.artistView.text = a.name
        }
        itemView.coverView.loadFromUrl(picasso, album.coverMedium)
    }
}