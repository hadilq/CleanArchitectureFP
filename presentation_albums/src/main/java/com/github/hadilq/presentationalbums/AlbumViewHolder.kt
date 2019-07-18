/***
 * Copyright 2019 Hadi Lashkari Ghouchani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * */
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

    fun bindTo(a: Album) {
        album = a

        itemView.titleView.text = a.title
        a.artist?.let { artist ->
            itemView.artistView.text = artist.name
        }
        itemView.coverView.loadFromUrl(picasso, a.coverMedium)
    }
}