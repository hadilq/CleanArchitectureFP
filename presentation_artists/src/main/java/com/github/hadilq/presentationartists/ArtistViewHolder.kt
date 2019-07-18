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